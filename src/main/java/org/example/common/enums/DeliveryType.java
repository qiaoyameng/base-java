package org.example.common.enums;

public enum DeliveryType {
    SELF_PICKUP("到店自取"),
    HOME_SERVICE("上门取送");

    private final String description;

    DeliveryType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
