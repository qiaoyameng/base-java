package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.CreateOrderDTO;
import org.example.dto.OrderDTO;
import org.example.dto.OrderItemDTO;
import org.example.dto.OrderQueryDTO;
import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.entity.Product;
import org.example.enums.OrderStatus;
import org.example.enums.PaymentMethod;
import org.example.common.PageResult;
import org.example.repository.OrderItemRepository;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;
import org.example.service.MemberService;
import org.example.service.OrderService;
import org.example.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final MemberService memberService;

    @Override
    @Transactional
    public OrderDTO createOrder(CreateOrderDTO createOrderDTO) {
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setCustomerId(createOrderDTO.getCustomerId());
        order.setCustomerName(createOrderDTO.getCustomerName());
        order.setCustomerPhone(createOrderDTO.getCustomerPhone());
        order.setPaymentMethod(createOrderDTO.getPaymentMethod());
        order.setPickupType(createOrderDTO.getPickupType());
        order.setRemark(createOrderDTO.getRemark());
        order.setAddress(createOrderDTO.getAddress());
        order.setUsedPoints(createOrderDTO.getUsePoints());
        order.setDeleted(false);
        order.setNotificationSent(false);

        if (createOrderDTO.getPaymentMethod() == PaymentMethod.ONLINE) {
            order.setStatus(OrderStatus.PENDING_PAYMENT);
        } else {
            order.setStatus(OrderStatus.PENDING_PREPARE);
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDTO itemDTO : createOrderDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("商品不存在: " + itemDTO.getProductId()));

            if (product.getStock() < itemDTO.getQuantity()) {
                throw new RuntimeException("商品库存不足: " + product.getName());
            }

            boolean deducted = productService.deductStock(product.getId(), itemDTO.getQuantity());
            if (!deducted) {
                throw new RuntimeException("扣减库存失败: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setSpecification(itemDTO.getSpecification());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
            orderItem.setOrder(order);
            orderItems.add(orderItem);

            totalAmount = totalAmount.add(orderItem.getTotalPrice());
        }

        order.setTotalAmount(totalAmount);

        BigDecimal pointsDiscount = BigDecimal.ZERO;
        if (createOrderDTO.getUsePoints() != null && createOrderDTO.getUsePoints() > 0) {
            pointsDiscount = memberService.calculatePointsDiscount(createOrderDTO.getCustomerId(), createOrderDTO.getUsePoints());
            memberService.deductPoints(createOrderDTO.getCustomerId(), createOrderDTO.getUsePoints());
        }
        order.setPointsDiscount(pointsDiscount);

        BigDecimal payAmount = totalAmount.subtract(pointsDiscount);
        if (payAmount.compareTo(BigDecimal.ZERO) < 0) {
            payAmount = BigDecimal.ZERO;
        }
        order.setPayAmount(payAmount);

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        return convertToDTO(savedOrder);
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        return convertToDTO(order);
    }

    @Override
    public OrderDTO getOrderByNo(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        return convertToDTO(order);
    }

    @Override
    public PageResult<OrderDTO> listOrders(OrderQueryDTO queryDTO) {
        Pageable pageable = PageRequest.of(queryDTO.getPageNum() - 1, queryDTO.getPageSize());
        Page<Order> orderPage = orderRepository.findByConditions(
                queryDTO.getOrderNo(),
                queryDTO.getCustomerId(),
                queryDTO.getCustomerName(),
                queryDTO.getStatus(),
                queryDTO.getPaymentMethod() != null ? queryDTO.getPaymentMethod().name() : null,
                queryDTO.getStartTime(),
                queryDTO.getEndTime(),
                pageable
        );

        List<OrderDTO> dtoList = orderPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        Page<OrderDTO> dtoPage = new org.springframework.data.domain.PageImpl<>(
                dtoList, pageable, orderPage.getTotalElements());

        return PageResult.of(dtoPage);
    }

    @Override
    public PageResult<OrderDTO> listCustomerOrders(Long customerId, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Order> orderPage = orderRepository.findByCustomerIdAndDeletedFalse(customerId, pageable);

        List<OrderDTO> dtoList = orderPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        Page<OrderDTO> dtoPage = new org.springframework.data.domain.PageImpl<>(
                dtoList, pageable, orderPage.getTotalElements());

        return PageResult.of(dtoPage);
    }

    @Override
    @Transactional
    public void payOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setStatus(OrderStatus.PENDING_PREPARE);
        order.setPaidTime(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void prepareOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (order.getStatus() != OrderStatus.PENDING_PREPARE) {
            throw new RuntimeException("订单状态不正确");
        }

        OrderStatus nextStatus = order.getStatus().nextStatus(order.getPaymentMethod());
        order.setStatus(nextStatus);
        order.setPreparedTime(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void shipOrder(Long orderId, String trackingNo) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (order.getStatus() != OrderStatus.PENDING_SHIPMENT) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setTrackingNo(trackingNo);
        order.setShippedTime(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING_PICKUP);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (order.getStatus() != OrderStatus.PENDING_PICKUP && order.getStatus() != OrderStatus.PENDING_SHIPMENT) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setStatus(OrderStatus.COMPLETED);
        order.setCompletedTime(LocalDateTime.now());
        orderRepository.save(order);

        for (OrderItem item : order.getItems()) {
            productService.increaseSales(item.getProductId(), item.getQuantity());
        }

        int points = order.getPayAmount().intValue();
        memberService.addPoints(order.getCustomerId(), points);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT &&
            order.getStatus() != OrderStatus.PENDING_PREPARE) {
            throw new RuntimeException("订单状态不允许取消");
        }

        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                product.setStock(product.getStock() + item.getQuantity());
                productRepository.save(product);
            }
        }

        if (order.getUsedPoints() != null && order.getUsedPoints() > 0) {
            memberService.addPoints(order.getCustomerId(), order.getUsedPoints().intValue());
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelledTime(LocalDateTime.now());
        order.setRemark(order.getRemark() + " [取消原因: " + reason + "]");
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void applyRefund(Long orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (order.getStatus() != OrderStatus.COMPLETED &&
            order.getStatus() != OrderStatus.PENDING_PICKUP &&
            order.getStatus() != OrderStatus.PENDING_SHIPMENT) {
            throw new RuntimeException("订单状态不允许退款");
        }

        order.setStatus(OrderStatus.REFUNDING);
        order.setRemark(order.getRemark() + " [退款原因: " + reason + "]");
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void approveRefund(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (order.getStatus() != OrderStatus.REFUNDING) {
            throw new RuntimeException("订单状态不正确");
        }

        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                product.setStock(product.getStock() + item.getQuantity());
                productRepository.save(product);
            }
        }

        int points = order.getPayAmount().intValue();
        memberService.deductPoints(order.getCustomerId(), (long) points);

        order.setStatus(OrderStatus.REFUNDED);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void rejectRefund(Long orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (order.getStatus() != OrderStatus.REFUNDING) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setStatus(OrderStatus.COMPLETED);
        order.setRemark(order.getRemark() + " [拒绝退款原因: " + reason + "]");
        orderRepository.save(order);
    }

    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", (int) (Math.random() * 10000));
        return "ORD" + timestamp + random;
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        BeanUtils.copyProperties(order, dto);
        if (order.getItems() != null) {
            List<OrderItemDTO> itemDTOs = order.getItems().stream()
                    .map(item -> {
                        OrderItemDTO itemDTO = new OrderItemDTO();
                        BeanUtils.copyProperties(item, itemDTO);
                        return itemDTO;
                    })
                    .collect(Collectors.toList());
            dto.setItems(itemDTOs);
        }
        return dto;
    }
}
