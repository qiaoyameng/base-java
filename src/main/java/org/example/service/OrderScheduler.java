package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Order;
import org.example.enums.OrderStatus;
import org.example.repository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderScheduler {
    private final OrderRepository orderRepository;
    private final NotificationService notificationService;

    @Scheduled(fixedRate = 60000)
    public void checkOrdersNeedReminder() {
        List<OrderStatus> statuses = List.of(OrderStatus.PENDING_PICKUP, OrderStatus.PENDING_SHIPMENT);
        List<Order> orders = orderRepository.findOrdersNeedReminder(statuses);

        for (Order order : orders) {
            try {
                notificationService.sendOrderReminder(order);
                order.setReminderTime(LocalDateTime.now());
                orderRepository.save(order);
                log.info("订单提醒已发送: {}", order.getOrderNo());
            } catch (Exception e) {
                log.error("发送订单提醒失败: {}", order.getOrderNo(), e);
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void cancelTimeoutOrders() {
        LocalDateTime timeout = LocalDateTime.now().minusMinutes(30);
        List<Order> orders = orderRepository.findTimeoutOrders(OrderStatus.PENDING_PAYMENT, timeout);

        for (Order order : orders) {
            try {
                order.setStatus(OrderStatus.CANCELLED);
                order.setCancelledTime(LocalDateTime.now());
                orderRepository.save(order);
                log.info("超时订单已取消: {}", order.getOrderNo());
            } catch (Exception e) {
                log.error("取消超时订单失败: {}", order.getOrderNo(), e);
            }
        }
    }
}
