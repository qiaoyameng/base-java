package org.example.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING_PAYMENT("待支付"),
    PENDING_ACCEPT("待接单"),
    IN_PROGRESS("洗护中"),
    PENDING_PICKUP("待取件"),
    COMPLETED("已完成"),
    CANCELLED("已取消"),
    REFUNDING("退款中"),
    REFUNDED("已退款");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
