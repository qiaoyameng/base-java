package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.OrderDTO;
import org.example.dto.OrderItemDTO;
import org.example.dto.RefundDTO;
import org.example.entity.*;
import org.example.enums.MemberLevel;
import org.example.enums.OrderStatus;
import org.example.enums.PaymentMethod;
import org.example.exception.BusinessException;
import org.example.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRefundRepository orderRefundRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Transactional
    public Order createOrder(OrderDTO dto) {
        Member member = memberRepository.findByIdAndDeletedFalse(dto.getMemberId())
                .orElseThrow(() -> new BusinessException("会员不存在"));

        if (dto.getPointsUsed() > 0 && member.getPoints() < dto.getPointsUsed()) {
            throw new BusinessException("积分不足");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findByIdAndDeletedFalse(itemDTO.getProductId())
                    .orElseThrow(() -> new BusinessException("商品不存在: " + itemDTO.getProductId()));

            if (product.getStock() < itemDTO.getQuantity()) {
                throw new BusinessException("商品库存不足: " + product.getName());
            }

            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(itemDTO.getQuantity());
            item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
            item.setSpecification(product.getSpecification());
            item.setProductImage(product.getImageUrl());
            orderItems.add(item);

            totalAmount = totalAmount.add(item.getSubtotal());
        }

        BigDecimal discountAmount = BigDecimal.ZERO;
        if (member.getLevel() != MemberLevel.NORMAL) {
            discountAmount = totalAmount.multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(member.getLevel().getDiscount())));
            discountAmount = discountAmount.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal pointsDiscount = BigDecimal.valueOf(dto.getPointsUsed()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        discountAmount = discountAmount.add(pointsDiscount);

        BigDecimal payAmount = totalAmount.subtract(discountAmount);
        if (payAmount.compareTo(BigDecimal.ZERO) < 0) {
            payAmount = BigDecimal.ZERO;
        }

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setMemberId(member.getId());
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setPayAmount(payAmount);
        order.setPointsUsed(dto.getPointsUsed());
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setRemark(dto.getRemark());
        order = orderRepository.save(order);

        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderItemRepository.save(item);

            Product product = productRepository.findByIdAndDeletedFalse(item.getProductId()).orElseThrow();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        if (dto.getPointsUsed() > 0) {
            memberService.deductPoints(member.getId(), dto.getPointsUsed(), "订单抵扣: " + order.getOrderNo());
        }

        return order;
    }

    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return timestamp + random;
    }

    @Transactional
    public Order payOrder(Long orderId) {
        Order order = getOrderById(orderId);
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BusinessException("订单状态不正确");
        }

        order.setStatus(OrderStatus.PENDING_PREPARATION);
        order.setPaidTime(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Transactional
    public Order prepareOrder(Long orderId) {
        Order order = getOrderById(orderId);
        if (order.getStatus() != OrderStatus.PENDING_PREPARATION) {
            throw new BusinessException("订单状态不正确");
        }

        order.setStatus(OrderStatus.PENDING_PICKUP);
        order.setPreparedTime(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Transactional
    public Order shipOrder(Long orderId) {
        Order order = getOrderById(orderId);
        if (order.getStatus() != OrderStatus.PENDING_PREPARATION) {
            throw new BusinessException("订单状态不正确");
        }

        order.setStatus(OrderStatus.PENDING_SHIPMENT);
        order.setPreparedTime(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Transactional
    public Order completeOrder(Long orderId) {
        Order order = getOrderById(orderId);
        if (order.getStatus() != OrderStatus.PENDING_PICKUP && order.getStatus() != OrderStatus.PENDING_SHIPMENT) {
            throw new BusinessException("订单状态不正确");
        }

        order.setStatus(OrderStatus.COMPLETED);
        order.setCompletedTime(LocalDateTime.now());
        orderRepository.save(order);

        Integer points = order.getPayAmount().intValue();
        memberService.addPoints(order.getMemberId(), points, orderId);
        memberService.updateTotalSpent(order.getMemberId(), order.getPayAmount());

        return order;
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);
        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("订单状态不正确");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelledTime(LocalDateTime.now());
        orderRepository.save(order);

        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        for (OrderItem item : items) {
            Product product = productRepository.findByIdAndDeletedFalse(item.getProductId()).orElseThrow();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        if (order.getPointsUsed() > 0) {
            Member member = memberRepository.findByIdAndDeletedFalse(order.getMemberId()).orElseThrow();
            member.setPoints(member.getPoints() + order.getPointsUsed());
            memberRepository.save(member);
        }

        return order;
    }

    @Transactional
    public OrderRefund applyRefund(Long orderId, RefundDTO dto) {
        Order order = getOrderById(orderId);
        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new BusinessException("只有已完成的订单才能申请退款");
        }

        if (dto.getRefundAmount().compareTo(order.getPayAmount()) > 0) {
            throw new BusinessException("退款金额不能超过实付金额");
        }

        if (orderRefundRepository.findByOrderId(orderId).isPresent()) {
            throw new BusinessException("该订单已存在退款申请");
        }

        order.setStatus(OrderStatus.REFUNDING);
        orderRepository.save(order);

        OrderRefund refund = new OrderRefund();
        refund.setOrderId(orderId);
        refund.setMemberId(order.getMemberId());
        refund.setRefundAmount(dto.getRefundAmount());
        refund.setReason(dto.getReason());
        return orderRefundRepository.save(refund);
    }

    @Transactional
    public OrderRefund approveRefund(Long refundId, Long reviewerId, String remark) {
        OrderRefund refund = orderRefundRepository.findById(refundId)
                .orElseThrow(() -> new BusinessException("退款申请不存在"));

        if (!"PENDING".equals(refund.getStatus())) {
            throw new BusinessException("退款申请已处理");
        }

        refund.setStatus("APPROVED");
        refund.setReviewerId(reviewerId);
        refund.setReviewTime(LocalDateTime.now());
        refund.setReviewRemark(remark);
        orderRefundRepository.save(refund);

        Order order = getOrderById(refund.getOrderId());
        order.setStatus(OrderStatus.REFUNDED);
        order.setRefundTime(LocalDateTime.now());
        orderRepository.save(order);

        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
        for (OrderItem item : items) {
            Product product = productRepository.findByIdAndDeletedFalse(item.getProductId()).orElseThrow();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        return refund;
    }

    @Transactional
    public OrderRefund rejectRefund(Long refundId, Long reviewerId, String remark) {
        OrderRefund refund = orderRefundRepository.findById(refundId)
                .orElseThrow(() -> new BusinessException("退款申请不存在"));

        if (!"PENDING".equals(refund.getStatus())) {
            throw new BusinessException("退款申请已处理");
        }

        refund.setStatus("REJECTED");
        refund.setReviewerId(reviewerId);
        refund.setReviewTime(LocalDateTime.now());
        refund.setReviewRemark(remark);
        orderRefundRepository.save(refund);

        Order order = getOrderById(refund.getOrderId());
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        return refund;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("订单不存在"));
    }

    public Order getOrderByOrderNo(String orderNo) {
        return orderRepository.findByOrderNoAndDeletedFalse(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));
    }

    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findByDeletedFalse(pageable);
    }

    public Page<Order> getOrdersByMember(Long memberId, Pageable pageable) {
        return orderRepository.findByMemberIdAndDeletedFalse(memberId, pageable);
    }

    public Page<Order> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByStatusAndDeletedFalse(status, pageable);
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public Page<OrderRefund> getRefundsByStatus(String status, Pageable pageable) {
        return orderRefundRepository.findByStatus(status, pageable);
    }

    public Page<OrderRefund> getMemberRefunds(Long memberId, Pageable pageable) {
        return orderRefundRepository.findByMemberIdOrderByCreateTimeDesc(memberId, pageable);
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        order.setDeleted(true);
        orderRepository.save(order);
    }
}
