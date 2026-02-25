package org.example.order.service;

import org.example.common.enums.OrderStatus;
import org.example.order.entity.Order;
import org.example.order.entity.OrderItem;
import org.example.order.entity.OrderStatusLog;
import org.example.order.repository.OrderItemRepository;
import org.example.order.repository.OrderRepository;
import org.example.order.repository.OrderStatusLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStatusLogRepository statusLogRepository;

    public OrderService(OrderRepository orderRepository, 
                       OrderItemRepository orderItemRepository,
                       OrderStatusLogRepository statusLogRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.statusLogRepository = statusLogRepository;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> findByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }

    public List<Order> findByMemberId(Long memberId) {
        return orderRepository.findByMemberIdOrderByCreatedAtDesc(memberId);
    }

    public List<Order> findByStoreId(Long storeId) {
        return orderRepository.findByStoreId(storeId);
    }

    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    @Transactional
    public Order createOrder(Order order, List<OrderItem> items) {
        order.setOrderNo(generateOrderNo());
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        
        BigDecimal totalAmount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount);
        
        Order savedOrder = orderRepository.save(order);
        
        for (OrderItem item : items) {
            item.setOrderId(savedOrder.getId());
            orderItemRepository.save(item);
        }
        
        logStatusChange(savedOrder.getId(), null, OrderStatus.PENDING_PAYMENT.name(), 
                       order.getMemberId(), "MEMBER", "订单创建");
        return savedOrder;
    }

    @Transactional
    public boolean payOrder(Long orderId, String paymentMethod, String paymentNo) {
        return orderRepository.findById(orderId).map(order -> {
            if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
                return false;
            }
            order.setStatus(OrderStatus.PENDING_ACCEPT);
            order.setPaymentMethod(paymentMethod);
            order.setPaymentNo(paymentNo);
            order.setPaymentTime(LocalDateTime.now());
            orderRepository.save(order);
            
            logStatusChange(orderId, OrderStatus.PENDING_PAYMENT.name(), 
                           OrderStatus.PENDING_ACCEPT.name(), order.getMemberId(), 
                           "MEMBER", "支付成功");
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean acceptOrder(Long orderId, Long employeeId) {
        return orderRepository.findById(orderId).map(order -> {
            if (order.getStatus() != OrderStatus.PENDING_ACCEPT) {
                return false;
            }
            order.setStatus(OrderStatus.IN_SERVICE);
            order.setEmployeeId(employeeId);
            order.setAcceptTime(LocalDateTime.now());
            orderRepository.save(order);
            
            logStatusChange(orderId, OrderStatus.PENDING_ACCEPT.name(), 
                           OrderStatus.IN_SERVICE.name(), employeeId, "EMPLOYEE", "接单成功");
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean completeService(Long orderId) {
        return orderRepository.findById(orderId).map(order -> {
            if (order.getStatus() != OrderStatus.IN_SERVICE) {
                return false;
            }
            order.setStatus(OrderStatus.PENDING_PICKUP);
            order.setCompleteTime(LocalDateTime.now());
            orderRepository.save(order);
            
            logStatusChange(orderId, OrderStatus.IN_SERVICE.name(), 
                           OrderStatus.PENDING_PICKUP.name(), order.getEmployeeId(), 
                           "EMPLOYEE", "洗护完成");
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean pickupOrder(Long orderId) {
        return orderRepository.findById(orderId).map(order -> {
            if (order.getStatus() != OrderStatus.PENDING_PICKUP) {
                return false;
            }
            order.setStatus(OrderStatus.COMPLETED);
            order.setDeliveryTime(LocalDateTime.now());
            orderRepository.save(order);
            
            logStatusChange(orderId, OrderStatus.PENDING_PICKUP.name(), 
                           OrderStatus.COMPLETED.name(), order.getMemberId(), 
                           "MEMBER", "取件完成");
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean cancelOrder(Long orderId, Long operatorId, String operatorType, String reason) {
        return orderRepository.findById(orderId).map(order -> {
            if (order.getStatus() == OrderStatus.COMPLETED || 
                order.getStatus() == OrderStatus.CANCELLED) {
                return false;
            }
            OrderStatus fromStatus = order.getStatus();
            order.setStatus(OrderStatus.CANCELLED);
            order.setCancelTime(LocalDateTime.now());
            order.setCancelReason(reason);
            orderRepository.save(order);
            
            logStatusChange(orderId, fromStatus.name(), OrderStatus.CANCELLED.name(), 
                           operatorId, operatorType, reason);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean updateOrder(Long orderId, Order order) {
        return orderRepository.findById(orderId).map(existing -> {
            existing.setPickupAddress(order.getPickupAddress());
            existing.setPickupContact(order.getPickupContact());
            existing.setPickupPhone(order.getPickupPhone());
            existing.setPickupTime(order.getPickupTime());
            existing.setDeliveryAddress(order.getDeliveryAddress());
            existing.setDeliveryContact(order.getDeliveryContact());
            existing.setDeliveryPhone(order.getDeliveryPhone());
            existing.setRemark(order.getRemark());
            orderRepository.save(existing);
            return true;
        }).orElse(false);
    }

    public List<OrderStatusLog> getStatusLogs(Long orderId) {
        return statusLogRepository.findByOrderIdOrderByOperateTimeDesc(orderId);
    }

    public List<Order> findOrdersToRemind() {
        LocalDateTime reminderTime = LocalDateTime.now().plusMinutes(30);
        return orderRepository.findByStatusAndCreatedAtBefore(OrderStatus.PENDING_PICKUP, reminderTime);
    }

    private void logStatusChange(Long orderId, String fromStatus, String toStatus, 
                                 Long operatorId, String operatorType, String remark) {
        OrderStatusLog log = new OrderStatusLog();
        log.setOrderId(orderId);
        log.setFromStatus(fromStatus);
        log.setToStatus(toStatus);
        log.setOperatorId(operatorId);
        log.setOperatorType(operatorType);
        log.setRemark(remark);
        log.setOperateTime(LocalDateTime.now());
        statusLogRepository.save(log);
    }

    private String generateOrderNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomStr = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "ORD" + dateStr + randomStr;
    }
}
