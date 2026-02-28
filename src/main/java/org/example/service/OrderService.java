package org.example.service;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.example.dto.OrderRequest;
import org.example.entity.*;
import org.example.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final CustomerCouponRepository customerCouponRepository;

    @Transactional
    public Orders createOrder(OrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("客户不存在"));

        Orders order = new Orders();
        order.setOrderNo(generateOrderNo());
        order.setCustomer(customer);
        order.setCustomerId(customer.getId());
        order.setPaymentMethod(Orders.PaymentMethod.valueOf(request.getPaymentMethod()));
        order.setDeliveryType(Orders.DeliveryType.valueOf(request.getDeliveryType()));
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setRemark(request.getRemark());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("商品不存在"));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("商品库存不足: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductSpec(product.getSpec());
            orderItem.setProductImage(product.getImages());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
            orderItem.setDiscountAmount(BigDecimal.ZERO);
            orderItems.add(orderItem);

            totalAmount = totalAmount.add(orderItem.getSubtotal());
        }

        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setPointsDeductAmount(BigDecimal.ZERO);

        BigDecimal discount = customer.getMemberLevel().getDiscount();
        if (discount.compareTo(BigDecimal.ONE) < 0) {
            BigDecimal discountAmount = totalAmount.multiply(BigDecimal.ONE.subtract(discount));
            order.setDiscountAmount(discountAmount);
        }

        if (request.getUsePoints() != null && request.getUsePoints() > 0) {
            if (customer.getAvailablePoints() < request.getUsePoints()) {
                throw new RuntimeException("积分不足");
            }
            BigDecimal pointsAmount = BigDecimal.valueOf(request.getUsePoints()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            order.setPointsDeductAmount(pointsAmount);
        }

        if (request.getUseCouponId() != null) {
            CustomerCoupon customerCoupon = customerCouponRepository.findById(request.getUseCouponId())
                    .orElseThrow(() -> new RuntimeException("优惠券不存在"));
            if (!customerCoupon.getStatus().equals(CustomerCoupon.CustomerCouponStatus.AVAILABLE)) {
                throw new RuntimeException("优惠券不可用");
            }
            Coupon coupon = customerCouponRepository.findCouponById(request.getUseCouponId());
            if (coupon != null) {
                if (coupon.getType() == Coupon.CouponType.FIXED_AMOUNT) {
                    order.setDiscountAmount(order.getDiscountAmount().add(coupon.getDiscountValue()));
                } else if (coupon.getType() == Coupon.CouponType.DISCOUNT) {
                    BigDecimal couponDiscount = totalAmount.multiply(BigDecimal.ONE.subtract(coupon.getDiscountValue().divide(BigDecimal.TEN)));
                    order.setDiscountAmount(order.getDiscountAmount().add(couponDiscount));
                }
                customerCoupon.setStatus(CustomerCoupon.CustomerCouponStatus.USED);
                customerCoupon.setUsedOrderId(order.getId());
                customerCouponRepository.save(customerCoupon);
            }
        }

        BigDecimal payableAmount = totalAmount.subtract(order.getDiscountAmount()).subtract(order.getPointsDeductAmount());
        order.setPayableAmount(payableAmount.max(BigDecimal.ZERO));

        if (Orders.PaymentMethod.STORE.equals(order.getPaymentMethod())) {
            order.setStatus(Orders.OrderStatus.PENDING_PREPARE);
            order.setPrepareTime(LocalDateTime.now());
        } else {
            order.setStatus(Orders.OrderStatus.PENDING_PAYMENT);
        }

        order.setPaidAmount(BigDecimal.ZERO);
        order = orderRepository.save(order);

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
            orderItem.setOrderId(order.getId());
            productRepository.decreaseStock(orderItem.getProductId(), orderItem.getQuantity());
        }
        orderItemRepository.saveAll(orderItems);
        order.setOrderItems(orderItems);

        return order;
    }

    @Transactional
    public Orders payment(String orderNo, Orders.PaymentMethod paymentMethod) {
        Orders order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!Orders.OrderStatus.PENDING_PAYMENT.equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许支付");
        }

        order.setPaymentMethod(paymentMethod);
        order.setPaidAmount(order.getPayableAmount());
        order.setPaymentTime(LocalDateTime.now());
        order.setStatus(Orders.OrderStatus.PENDING_PREPARE);
        order.setPrepareTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    @Transactional
    public Orders prepareComplete(String orderNo) {
        Orders order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!Orders.OrderStatus.PENDING_PREPARE.equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许完成备货");
        }

        if (Orders.DeliveryType.STORE_PICKUP.equals(order.getDeliveryType())) {
            order.setStatus(Orders.OrderStatus.PENDING_PICKUP);
        } else {
            order.setStatus(Orders.OrderStatus.PENDING_DELIVERY);
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Orders delivery(String orderNo) {
        Orders order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!Orders.OrderStatus.PENDING_DELIVERY.equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许发货");
        }

        order.setStatus(Orders.OrderStatus.DELIVERING);
        order.setDeliveryTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    @Transactional
    public Orders complete(String orderNo) {
        Orders order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!Orders.OrderStatus.PENDING_PICKUP.equals(order.getStatus())
                && !Orders.OrderStatus.DELIVERING.equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许完成");
        }

        order.setStatus(Orders.OrderStatus.COMPLETED);
        order.setCompleteTime(LocalDateTime.now());

        Customer customer = customerRepository.findById(order.getCustomerId()).orElse(null);
        if (customer != null) {
            int earnedPoints = order.getPaidAmount().intValue() * customer.getMemberLevel().getPointsRate() / 10;
            customer.setTotalPoints(customer.getTotalPoints() + earnedPoints);
            customer.setAvailablePoints(customer.getAvailablePoints() - (order.getPointsDeductAmount() != null ? order.getPointsDeductAmount().intValue() * 100 : 0) + earnedPoints);
            customer.setTotalAmount(customer.getTotalAmount().add(order.getPaidAmount()));
            updateMemberLevel(customer);
            customerRepository.save(customer);
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Orders cancel(String orderNo, String reason) {
        Orders order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (Orders.OrderStatus.COMPLETED.equals(order.getStatus())
                || Orders.OrderStatus.CANCELLED.equals(order.getStatus())
                || Orders.OrderStatus.REFUNDED.equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许取消");
        }

        order.setStatus(Orders.OrderStatus.CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        order.setCancelReason(reason);

        for (OrderItem item : order.getOrderItems()) {
            productRepository.increaseStock(item.getProductId(), item.getQuantity());
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Orders applyRefund(String orderNo, String reason) {
        Orders order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!Orders.OrderStatus.COMPLETED.equals(order.getStatus())) {
            throw new RuntimeException("只有已完成订单可以申请退款");
        }

        order.setStatus(Orders.OrderStatus.REFUNDING);
        order.setCancelReason(reason);

        return orderRepository.save(order);
    }

    @Transactional
    public Orders approveRefund(String orderNo, Boolean approved) {
        Orders order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!Orders.OrderStatus.REFUNDING.equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确");
        }

        if (approved) {
            order.setStatus(Orders.OrderStatus.REFUNDED);
            order.setCancelTime(LocalDateTime.now());

            for (OrderItem item : order.getOrderItems()) {
                productRepository.increaseStock(item.getProductId(), item.getQuantity());
            }

            Customer customer = customerRepository.findById(order.getCustomerId()).orElse(null);
            if (customer != null) {
                customer.setStoredValue(customer.getStoredValue().add(order.getPaidAmount()));
                customerRepository.save(customer);
            }
        } else {
            order.setStatus(Orders.OrderStatus.COMPLETED);
        }

        return orderRepository.save(order);
    }

    public Orders findByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo).orElse(null);
    }

    public Orders findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Page<Orders> findByCustomerId(Long customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return orderRepository.findByCustomerId(customerId, pageable);
    }

    public Page<Orders> findByCustomerIdAndStatus(Long customerId, Orders.OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return orderRepository.findByCustomerIdAndStatus(customerId, status, pageable);
    }

    public Page<Orders> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return orderRepository.findAll(pageable);
    }

    public List<Orders> findOrdersToNotify() {
        LocalDateTime notifyTime = LocalDateTime.now().minusMinutes(15);
        return orderRepository.findOrdersToNotify(notifyTime);
    }

    @Transactional
    public void markAsNotified(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setNotifiedPrepare(true);
            orderRepository.save(order);
        }
    }

    public List<Orders> findExpiredPendingPayment(int minutes) {
        LocalDateTime expireTime = LocalDateTime.now().minusMinutes(minutes);
        return orderRepository.findByStatusAndCreateTimeBefore(Orders.OrderStatus.PENDING_PAYMENT, expireTime);
    }

    private String generateOrderNo() {
        return IdUtil.getSnowflakeNextIdStr();
    }

    private void updateMemberLevel(Customer customer) {
        BigDecimal total = customer.getTotalAmount();
        if (total.compareTo(BigDecimal.valueOf(10000)) >= 0) {
            customer.setMemberLevel(Customer.MemberLevel.GOLD);
        } else if (total.compareTo(BigDecimal.valueOf(2000)) >= 0) {
            customer.setMemberLevel(Customer.MemberLevel.SILVER);
        } else {
            customer.setMemberLevel(Customer.MemberLevel.NORMAL);
        }
    }
}
