package org.example.enums;

import lombok.Getter;

@Getter
public enum CouponType {
    FIXED_AMOUNT("满减"),
    DISCOUNT("折扣"),
    FREE_DELIVERY("免运费");

    private final String description;

    CouponType(String description) {
        this.description = description;
    }
}
