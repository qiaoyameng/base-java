package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Order;
import org.example.enums.OrderStatus;
import org.example.repository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderNotificationService {

    private final OrderRepository orderRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void sendPreparationNotification() {
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);

        List<Order> orders = orderRepository.findByStatusAndNotificationSentFalseAndPreparedTimeBefore(
                OrderStatus.PENDING_PICKUP, fifteenMinutesAgo);

        for (Order order : orders) {
            try {
                sendNotification(order);
                order.setNotificationSent(true);
                order.setNotificationTime(LocalDateTime.now());
                orderRepository.save(order);
            } catch (Exception e) {
                log.error("发送订单通知失败, orderId: {}", order.getId(), e);
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void autoCancelUnpaidOrders() {
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);

        List<Order> orders = orderRepository.findByStatusAndPaidTimeBefore(
                OrderStatus.PENDING_PAYMENT, thirtyMinutesAgo);

        for (Order order : orders) {
            try {
                order.setStatus(OrderStatus.CANCELLED);
                order.setCancelledTime(LocalDateTime.now());
                order.setRemark("系统自动取消：超时未支付");
                orderRepository.save(order);
                log.info("订单自动取消, orderNo: {}", order.getOrderNo());
            } catch (Exception e) {
                log.error("自动取消订单失败, orderId: {}", order.getId(), e);
            }
        }
    }

    private void sendNotification(Order order) {
        log.info("发送订单备货完成通知, orderNo: {}, customerPhone: {}, pickupType: {}",
                order.getOrderNo(), order.getCustomerPhone(), order.getPickupType());
    }
}
