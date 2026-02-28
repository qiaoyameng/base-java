package org.example.enums;

public enum MemberLevel {
    NORMAL("普通会员", 0, 1.0, 0),
    SILVER("银卡会员", 1000, 0.95, 5),
    GOLD("金卡会员", 5000, 0.90, 10);

    private final String description;
    private final int minConsumption;
    private final double discountRate;
    private final int pointsMultiplier;

    MemberLevel(String description, int minConsumption, double discountRate, int pointsMultiplier) {
        this.description = description;
        this.minConsumption = minConsumption;
        this.discountRate = discountRate;
        this.pointsMultiplier = pointsMultiplier;
    }

    public String getDescription() {
        return description;
    }

    public int getMinConsumption() {
        return minConsumption;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public int getPointsMultiplier() {
        return pointsMultiplier;
    }

    public static MemberLevel getByConsumption(double totalConsumption) {
        if (totalConsumption >= GOLD.minConsumption) {
            return GOLD;
        } else if (totalConsumption >= SILVER.minConsumption) {
            return SILVER;
        }
        return NORMAL;
    }
}
