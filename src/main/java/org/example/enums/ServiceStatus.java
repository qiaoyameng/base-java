package org.example.enums;

import lombok.Getter;

@Getter
public enum ServiceStatus {
    PENDING("待接单"),
    IN_PROGRESS("洗护中"),
    COMPLETED("已完成"),
    PICKED_UP("已取件");

    private final String description;

    ServiceStatus(String description) {
        this.description = description;
    }
}
