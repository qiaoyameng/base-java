package org.example.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Notification;
import org.example.entity.Orders;
import org.example.repository.NotificationRepository;
import org.example.repository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderScheduledTask {

    private final OrderRepository orderRepository;
    private final NotificationRepository notificationRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cancelUnpaidOrders() {
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);
        List<Orders> unpaidOrders = orderRepository.findByStatusAndCreateTimeBefore(
                Orders.OrderStatus.PENDING_PAYMENT, fifteenMinutesAgo);

        for (Orders order : unpaidOrders) {
            if (order.getPaymentTime() == null) {
                order.setStatus(Orders.OrderStatus.CANCELLED);
                order.setCancelReason("支付超时自动取消");
                order.setCancelTime(LocalDateTime.now());
                orderRepository.save(order);
                log.info("Order {} auto-cancelled due to payment timeout", order.getOrderNo());

                createNotification(order.getCustomerId(),
                        "订单自动取消通知",
                        "您的订单 " + order.getOrderNo() + " 因支付超时已自动取消",
                        Notification.NotificationType.ORDER);
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void sendReadyNotification() {
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);
        List<Orders> orders = orderRepository.findByStatusAndUpdateTimeBefore(
                Orders.OrderStatus.READY_FOR_PICKUP, fifteenMinutesAgo);
        orders.addAll(orderRepository.findByStatusAndUpdateTimeBefore(
                Orders.OrderStatus.READY_FOR_SHIPPING, fifteenMinutesAgo));

        for (Orders order : orders) {
            String key = "order_notify_sent:" + order.getId();
            if (!notificationRepository.existsByOrderIdAndType(
                    order.getId(), Notification.NotificationType.REMINDER)) {

                String message = switch (order.getStatus()) {
                    case READY_FOR_PICKUP -> "您的订单 " + order.getOrderNo() + " 已备货完成，请尽快取货！";
                    case READY_FOR_SHIPPING -> "您的订单 " + order.getOrderNo() + " 已发货完成，请查收物流信息";
                    default -> "订单状态更新通知";
                };

                createNotification(order.getCustomerId(), "订单状态提醒", message, Notification.NotificationType.REMINDER);
                log.info("Reminder notification sent for order {}", order.getOrderNo());
            }
        }
    }

    private void createNotification(Long customerId, String title, String content, Notification.NotificationType type) {
        Notification notification = new Notification();
        notification.setCustomerId(customerId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setIsRead(false);
        notification.setIsPush(true);
        notificationRepository.save(notification);
    }
}
