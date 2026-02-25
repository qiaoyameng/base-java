package org.example.common.enums;

public enum CouponType {
    FULL_REDUCTION("满减"),
    DISCOUNT("折扣"),
    FREE_SHIPPING("免运费");

    private final String description;

    CouponType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
