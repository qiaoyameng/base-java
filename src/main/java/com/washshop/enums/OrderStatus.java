package com.washshop.enums;

public enum OrderStatus {
    PENDING_PAYMENT(0, "待支付"),
    PENDING_ACCEPT(1, "待接单"),
    WASHING(2, "洗护中"),
    READY(3, "待取件"),
    DELIVERING(4, "配送中"),
    COMPLETED(5, "已完成"),
    CANCELLED(6, "已取消"),
    REFUNDING(7, "退款中"),
    REFUNDED(8, "已退款");

    private final Integer code;
    private final String desc;

    OrderStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static OrderStatus fromCode(Integer code) {
        for (OrderStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
