package org.example.common.enums;

public enum MemberLevel {
    NORMAL("普通", 1),
    SILVER("银卡", 2),
    GOLD("金卡", 3);

    private final String description;
    private final Integer level;

    MemberLevel(String description, Integer level) {
        this.description = description;
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public Integer getLevel() {
        return level;
    }
}
