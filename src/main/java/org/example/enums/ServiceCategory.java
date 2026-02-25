package org.example.enums;

import lombok.Getter;

@Getter
public enum ServiceCategory {
    DRY_CLEANING("干洗"),
    WASHING("水洗"),
    IRONING("熨烫"),
    CARE("洗护保养"),
    LUXURY_CARE("奢侈品护理");

    private final String description;

    ServiceCategory(String description) {
        this.description = description;
    }
}
