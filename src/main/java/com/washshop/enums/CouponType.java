package com.washshop.enums;

public enum CouponType {
    FULL_REDUCTION(1, "满减券"),
    DISCOUNT(2, "折扣券"),
    FREE_SHIPPING(3, "免运费券");

    private final Integer code;
    private final String desc;

    CouponType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static CouponType fromCode(Integer code) {
        for (CouponType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
