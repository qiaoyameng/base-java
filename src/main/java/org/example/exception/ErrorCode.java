package org.example.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SYSTEM_ERROR(500, "系统错误"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),

    PRODUCT_NOT_FOUND(1001, "商品不存在"),
    PRODUCT_STOCK_NOT_ENOUGH(1002, "商品库存不足"),
    PRODUCT_OFF_SHELF(1003, "商品已下架"),

    ORDER_NOT_FOUND(2001, "订单不存在"),
    ORDER_STATUS_ERROR(2002, "订单状态错误"),
    ORDER_PAYMENT_FAILED(2003, "订单支付失败"),
    ORDER_CANCEL_FAILED(2004, "订单取消失败"),

    CUSTOMER_NOT_FOUND(3001, "会员不存在"),
    CUSTOMER_DISABLED(3002, "会员已禁用"),
    CUSTOMER_POINTS_NOT_ENOUGH(3003, "积分不足"),
    CUSTOMER_BALANCE_NOT_ENOUGH(3004, "余额不足"),

    COUPON_NOT_FOUND(4001, "优惠券不存在"),
    COUPON_EXPIRED(4002, "优惠券已过期"),
    COUPON_USED(4003, "优惠券已使用"),
    COUPON_NOT_APPLICABLE(4004, "优惠券不适用"),

    REVIEW_NOT_FOUND(5001, "评价不存在"),
    TRANSACTION_NOT_FOUND(6001, "消费记录不存在"),

    SHARE_NOT_FOUND(7001, "分享不存在"),
    PHOTO_NOT_FOUND(7002, "照片不存在"),
    ACTIVITY_NOT_FOUND(7003, "活动不存在");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
