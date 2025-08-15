package com.gym.gymapp.model;

public enum TrainingType {
    FITNESS("fitness"),
    YOGA("yoga"),
    ZUMBA("zumba"),
    STRETCHING("stretching"),
    RESISTANCE("resistance");

    private final String value;

    TrainingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TrainingType fromString(String value) {
        for (TrainingType type : TrainingType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown training type: " + value);
    }
}
