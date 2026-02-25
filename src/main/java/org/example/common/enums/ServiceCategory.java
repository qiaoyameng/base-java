package org.example.common.enums;

public enum ServiceCategory {
    DRY_CLEAN("干洗"),
    WASH("水洗"),
    IRON("熨烫"),
    CARE("洗护保养"),
    LUXURY("奢侈品护理");

    private final String description;

    ServiceCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
