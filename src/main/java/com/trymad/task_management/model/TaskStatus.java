package com.trymad.task_management.model;

public enum TaskStatus {
    NEW("NEW"),
    IN_PROGRESS("IN_PROGRESS"),
    PAUSED("PAUSED"),
    CANCELED("CANCELED"),
    COMPLETED("COMPLETED");

    private final String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static TaskStatus fromString(String status) {
        try {
            return TaskStatus.valueOf(status.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown status: " + status, e);
        }
    }
}
