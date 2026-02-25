package org.example.enums;

import lombok.Getter;

@Getter
public enum CouponStatus {
    ACTIVE("可使用"),
    USED("已使用"),
    EXPIRED("已过期");

    private final String description;

    CouponStatus(String description) {
        this.description = description;
    }
}
