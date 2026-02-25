package com.washshop.enums;

public enum DeliveryType {
    SELF_PICKUP(1, "到店自取"),
    DOOR_TO_DOOR(2, "上门取送");

    private final Integer code;
    private final String desc;

    DeliveryType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static DeliveryType fromCode(Integer code) {
        for (DeliveryType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
