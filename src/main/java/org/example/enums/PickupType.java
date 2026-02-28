package org.example.enums;

public enum PickupType {
    SELF_PICKUP("到店自取"),
    DELIVERY("快递配送");

    private final String description;

    PickupType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
