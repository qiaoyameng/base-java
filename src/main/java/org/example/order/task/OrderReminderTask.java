package org.example.order.task;

import org.example.common.enums.OrderStatus;
import org.example.order.entity.Order;
import org.example.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderReminderTask {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderReminderTask.class);
    
    private final OrderRepository orderRepository;

    public OrderReminderTask(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void checkPendingPickupOrders() {
        logger.info("开始检查待取件订单提醒...");
        
        LocalDateTime reminderTime = LocalDateTime.now().plusMinutes(30);
        List<Order> orders = orderRepository.findByStatusAndCreatedAtBefore(
                OrderStatus.PENDING_PICKUP, 
                LocalDateTime.now().minusHours(2)
        );
        
        for (Order order : orders) {
            sendPickupReminder(order);
        }
        
        logger.info("检查完成，共处理 {} 个订单提醒", orders.size());
    }

    private void sendPickupReminder(Order order) {
        logger.info("发送取件提醒: 订单号={}, 会员ID={}", order.getOrderNo(), order.getMemberId());
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void expireCoupons() {
        logger.info("开始处理过期优惠券...");
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void updateMemberLevels() {
        logger.info("开始更新会员等级...");
    }
}
