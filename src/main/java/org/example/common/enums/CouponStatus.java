package org.example.common.enums;

public enum CouponStatus {
    AVAILABLE("可用"),
    USED("已使用"),
    EXPIRED("已过期");

    private final String description;

    CouponStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
