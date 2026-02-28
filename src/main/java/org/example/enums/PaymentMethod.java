package org.example.enums;

public enum PaymentMethod {
    ONLINE("线上支付"),
    IN_STORE("到店付款");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
