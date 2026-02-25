package org.example.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    ADMIN("管理员"),
    STORE_MANAGER("店长"),
    STAFF("员工"),
    CUSTOMER("客户");

    private final String description;

    RoleType(String description) {
        this.description = description;
    }
}
