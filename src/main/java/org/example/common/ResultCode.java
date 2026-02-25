package org.example.common;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),

    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),

    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    INVALID_PASSWORD(1003, "密码错误"),
    INVALID_TOKEN(1004, "无效的Token"),
    TOKEN_EXPIRED(1005, "Token已过期"),

    SERVICE_NOT_FOUND(2001, "服务不存在"),
    ORDER_NOT_FOUND(2002, "订单不存在"),
    ORDER_STATUS_ERROR(2003, "订单状态错误"),
    ORDER_CANNOT_CANCEL(2004, "订单无法取消"),

    COUPON_NOT_FOUND(3001, "优惠券不存在"),
    COUPON_EXPIRED(3002, "优惠券已过期"),
    COUPON_USED(3003, "优惠券已使用"),
    COUPON_NOT_APPLICABLE(3004, "优惠券不适用"),

    STORED_CARD_INSUFFICIENT(4001, "储值卡余额不足"),

    INVALID_PARAMETER(9001, "参数错误"),
    OPERATION_NOT_ALLOWED(9002, "不允许的操作"),
    ;

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
