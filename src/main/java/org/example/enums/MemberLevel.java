package org.example.enums;

public enum MemberLevel {
    NORMAL("普通", 1.0, 0),
    SILVER("银卡", 0.95, 1000),
    GOLD("金卡", 0.90, 5000);

    private final String description;
    private final double discount;
    private final int requiredPoints;

    MemberLevel(String description, double discount, int requiredPoints) {
        this.description = description;
        this.discount = discount;
        this.requiredPoints = requiredPoints;
    }

    public String getDescription() {
        return description;
    }

    public double getDiscount() {
        return discount;
    }

    public int getRequiredPoints() {
        return requiredPoints;
    }

    public static MemberLevel getLevelByPoints(int points) {
        if (points >= GOLD.requiredPoints) {
            return GOLD;
        } else if (points >= SILVER.requiredPoints) {
            return SILVER;
        }
        return NORMAL;
    }
}
