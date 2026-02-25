package com.washshop.enums;

public enum MemberLevel {
    NORMAL(0, "普通会员", 0),
    SILVER(1, "银卡会员", 1000),
    GOLD(2, "金卡会员", 5000);

    private final Integer code;
    private final String desc;
    private final Integer minPoints;

    MemberLevel(Integer code, String desc, Integer minPoints) {
        this.code = code;
        this.desc = desc;
        this.minPoints = minPoints;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getMinPoints() {
        return minPoints;
    }

    public static MemberLevel fromCode(Integer code) {
        for (MemberLevel level : values()) {
            if (level.code.equals(code)) {
                return level;
            }
        }
        return NORMAL;
    }

    public static MemberLevel fromPoints(Integer points) {
        if (points >= GOLD.minPoints) {
            return GOLD;
        } else if (points >= SILVER.minPoints) {
            return SILVER;
        }
        return NORMAL;
    }
}
