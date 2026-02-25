package com.washshop.task;

import com.washshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderReminderTask {

    @Autowired
    private OrderService orderService;

    @Scheduled(fixedRate = 60000)
    public void sendReminders() {
        orderService.sendOrderReminder();
    }
}
