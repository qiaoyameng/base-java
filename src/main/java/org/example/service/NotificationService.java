package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.Order;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void sendOrderReminder(Order order) {
        log.info("发送订单提醒: 订单号={}, 会员ID={}, 状态={}", 
                order.getOrderNo(), order.getMemberId(), order.getStatus());
    }

    public void sendPaymentSuccess(Order order) {
        log.info("发送支付成功通知: 订单号={}, 会员ID={}", 
                order.getOrderNo(), order.getMemberId());
    }

    public void sendOrderComplete(Order order) {
        log.info("发送订单完成通知: 订单号={}, 会员ID={}", 
                order.getOrderNo(), order.getMemberId());
    }
}
