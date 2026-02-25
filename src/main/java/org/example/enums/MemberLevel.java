package org.example.enums;

import lombok.Getter;

@Getter
public enum MemberLevel {
    NORMAL("普通会员", 0, 0),
    SILVER("银卡会员", 1000, 5),
    GOLD("金卡会员", 5000, 10);

    private final String description;
    private final Integer minPoints;
    private final Integer discount;

    MemberLevel(String description, Integer minPoints, Integer discount) {
        this.description = description;
        this.minPoints = minPoints;
        this.discount = discount;
    }

    public static MemberLevel getByPoints(Integer points) {
        if (points >= GOLD.minPoints) {
            return GOLD;
        } else if (points >= SILVER.minPoints) {
            return SILVER;
        }
        return NORMAL;
    }
}
