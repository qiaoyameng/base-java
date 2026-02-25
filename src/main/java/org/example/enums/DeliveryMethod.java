package org.example.enums;

import lombok.Getter;

@Getter
public enum DeliveryMethod {
    SELF_PICKUP("到店自取"),
    HOME_DELIVERY("上门取送");

    private final String description;

    DeliveryMethod(String description) {
        this.description = description;
    }
}
