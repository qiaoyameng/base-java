package org.example.service;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.OrderRequest;
import org.example.dto.OrderResponse;
import org.example.entity.*;
import org.example.enums.OrderStatus;
import org.example.exception.BusinessException;
import org.example.mapstruct.OrderMapper;
import org.example.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WashServiceRepository washServiceRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final CouponUsageRepository couponUsageRepository;
    private final StoredCardRepository storedCardRepository;
    private final PointsTransactionRepository pointsTransactionRepository;
    private final ServiceRecordRepository serviceRecordRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponse createOrder(Long customerId, OrderRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("客户不存在"));
        
        WashService service = washServiceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new BusinessException("服务不存在"));

        Store store = service.getStore();
        if (store == null) {
            store = storeRepository.findById(request.getStoreId())
                    .orElseThrow(() -> new BusinessException("门店不存在"));
        }

        BigDecimal unitPrice = service.getPrice();
        BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal pointsDeduction = BigDecimal.ZERO;
        BigDecimal actualAmount = totalAmount;

        if (request.getCouponUsageId() != null) {
            CouponUsage couponUsage = couponUsageRepository.findById(request.getCouponUsageId())
                    .orElseThrow(() -> new BusinessException("优惠券不存在"));
            if (couponUsage.getUsedTime() != null) {
                throw new BusinessException("优惠券已使用");
            }
            Coupon coupon = couponUsage.getCoupon();
            if (totalAmount.compareTo(coupon.getMinAmount()) < 0) {
                throw new BusinessException("订单金额不满足优惠券最低使用条件");
            }
            switch (coupon.getType()) {
                case FIXED_AMOUNT -> discountAmount = discountAmount.add(coupon.getDiscountValue());
                case DISCOUNT -> discountAmount = discountAmount.add(
                        totalAmount.multiply(BigDecimal.ONE.subtract(coupon.getDiscountValue()))
                );
                case FREE_DELIVERY -> {}
            }
        }

        if (request.getPointsToUse() != null && request.getPointsToUse().compareTo(BigDecimal.ZERO) > 0) {
            if (customer.getAvailablePoints().compareTo(request.getPointsToUse()) < 0) {
                throw new BusinessException("可用积分不足");
            }
            pointsDeduction = request.getPointsToUse().divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
        }

        if (request.getStoredCardId() != null) {
            StoredCard storedCard = storedCardRepository.findById(request.getStoredCardId())
                    .orElseThrow(() -> new BusinessException("储值卡不存在"));
            if (storedCard.getCustomer().getId() != customer.getId()) {
                throw new BusinessException("储值卡不属于该客户");
            }
        }

        actualAmount = totalAmount.subtract(discountAmount).subtract(pointsDeduction);
        if (actualAmount.compareTo(BigDecimal.ZERO) < 0) {
            actualAmount = BigDecimal.ZERO;
        }

        WashOrder order = new WashOrder();
        order.setOrderNo(generateOrderNo());
        order.setCustomer(customer);
        order.setService(service);
        order.setStore(store);
        order.setServiceName(service.getName());
        order.setClothesDescription(request.getClothesDescription());
        order.setQuantity(request.getQuantity());
        order.setUnitPrice(unitPrice);
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setPointsDeduction(pointsDeduction);
        order.setActualAmount(actualAmount);
        order.setDeliveryMethod(request.getDeliveryMethod());
        order.setPickupAddress(request.getPickupAddress());
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setRemark(request.getRemark());

        WashOrder saved = orderRepository.save(order);
        log.info("创建订单成功: orderNo={}, customerId={}", saved.getOrderNo(), customerId);
        return orderMapper.toResponse(saved);
    }

    @Transactional
    public OrderResponse payOrder(Long orderId) {
        WashOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BusinessException("订单状态不允许支付");
        }

        order.setStatus(OrderStatus.PENDING_ACCEPT);
        order.setPaymentTime(LocalDateTime.now());
        
        LocalDateTime estimated = LocalDateTime.now().plusMinutes(order.getService().getDurationMinutes());
        order.setEstimatedCompleteTime(estimated);

        WashOrder saved = orderRepository.save(order);
        log.info("订单支付成功: orderNo={}", saved.getOrderNo());
        return orderMapper.toResponse(saved);
    }

    @Transactional
    public OrderResponse acceptOrder(Long orderId) {
        WashOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        if (order.getStatus() != OrderStatus.PENDING_ACCEPT) {
            throw new BusinessException("订单状态不允许接单");
        }

        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setAcceptTime(LocalDateTime.now());

        WashOrder saved = orderRepository.save(order);
        log.info("接单成功: orderNo={}", saved.getOrderNo());
        return orderMapper.toResponse(saved);
    }

    @Transactional
    public OrderResponse startWash(Long orderId) {
        WashOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new BusinessException("订单状态不允许开始洗护");
        }

        order.setStartTime(LocalDateTime.now());

        WashOrder saved = orderRepository.save(order);
        log.info("开始洗护: orderNo={}", saved.getOrderNo());
        return orderMapper.toResponse(saved);
    }

    @Transactional
    public OrderResponse completeOrder(Long orderId) {
        WashOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new BusinessException("订单状态不允许完成");
        }

        order.setStatus(OrderStatus.PENDING_PICKUP);
        order.setCompletedTime(LocalDateTime.now());

        WashOrder saved = orderRepository.save(order);

        ServiceRecord record = new ServiceRecord();
        record.setOrder(saved);
        record.setService(saved.getService());
        record.setCustomer(saved.getCustomer());
        record.setStore(saved.getStore());
        record.setCheckInTime(order.getStartTime());
        record.setDeliveryTime(LocalDateTime.now());
        record.setActualDurationMinutes(
                (int) java.time.Duration.between(order.getStartTime(), LocalDateTime.now()).toMinutes()
        );
        serviceRecordRepository.save(record);

        Customer customer = order.getCustomer();
        customer.setOrderCount(customer.getOrderCount() + 1);
        customer.setTotalConsumption(customer.getTotalConsumption().add(order.getActualAmount()));

        BigDecimal pointsEarned = order.getActualAmount().divide(BigDecimal.TEN, 0, java.math.RoundingMode.DOWN);
        BigDecimal pointsBefore = customer.getAvailablePoints();
        BigDecimal pointsAfter = pointsBefore.add(pointsEarned);
        customer.setTotalPoints(customer.getTotalPoints().add(pointsEarned));
        customer.setAvailablePoints(pointsAfter);
        customerRepository.save(customer);

        PointsTransaction pt = new PointsTransaction();
        pt.setCustomer(customer);
        pt.setPoints(pointsEarned);
        pt.setPointsBefore(pointsBefore);
        pt.setPointsAfter(pointsAfter);
        pt.setType("EARN");
        pt.setOrderNo(saved.getOrderNo());
        pt.setDescription("订单消费获得积分");
        pointsTransactionRepository.save(pt);

        log.info("订单洗护完成: orderNo={}, 获得积分: {}", saved.getOrderNo(), pointsEarned);
        return orderMapper.toResponse(saved);
    }

    @Transactional
    public OrderResponse pickupOrder(Long orderId) {
        WashOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        if (order.getStatus() != OrderStatus.PENDING_PICKUP) {
            throw new BusinessException("订单状态不允许取件");
        }

        order.setStatus(OrderStatus.COMPLETED);
        order.setPickupTime(LocalDateTime.now());

        WashOrder saved = orderRepository.save(order);
        log.info("订单取件完成: orderNo={}", saved.getOrderNo());
        return orderMapper.toResponse(saved);
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        WashOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        if (order.getStatus() == OrderStatus.IN_PROGRESS ||
            order.getStatus() == OrderStatus.PENDING_PICKUP ||
            order.getStatus() == OrderStatus.COMPLETED) {
            throw new BusinessException("当前订单状态不允许取消");
        }

        order.setStatus(OrderStatus.CANCELLED);

        WashOrder saved = orderRepository.save(order);
        log.info("取消订单: orderNo={}", saved.getOrderNo());
        return orderMapper.toResponse(saved);
    }

    @Transactional
    public OrderResponse requestRefund(Long orderId, String reason) {
        WashOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        if (order.getStatus() != OrderStatus.PENDING_ACCEPT &&
            order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new BusinessException("当前订单状态不允许申请退款");
        }

        order.setStatus(OrderStatus.REFUNDING);

        WashOrder saved = orderRepository.save(order);
        log.info("申请退款: orderNo={}, reason={}", saved.getOrderNo(), reason);
        return orderMapper.toResponse(saved);
    }

    public OrderResponse getOrderById(Long orderId) {
        WashOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        return orderMapper.toResponse(order);
    }

    public OrderResponse getOrderByNo(String orderNo) {
        WashOrder order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        return orderMapper.toResponse(order);
    }

    public List<OrderResponse> getOrdersByCustomer(Long customerId) {
        List<WashOrder> orders = orderRepository.findByCustomerId(customerId);
        return orderMapper.toResponseList(orders);
    }

    public List<OrderResponse> getOrdersByCustomerAndStatus(Long customerId, OrderStatus status) {
        List<WashOrder> orders = orderRepository.findByCustomerIdAndStatus(customerId, status);
        return orderMapper.toResponseList(orders);
    }

    public List<OrderResponse> getOrdersByStore(Long storeId) {
        List<WashOrder> orders = orderRepository.findByStoreId(storeId);
        return orderMapper.toResponseList(orders);
    }

    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        List<WashOrder> orders = orderRepository.findByStatus(status);
        return orderMapper.toResponseList(orders);
    }

    private String generateOrderNo() {
        return IdUtil.getSnowflakeNextIdStr();
    }
}
