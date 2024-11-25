package com.trymad.task_management.model;

public enum TaskPriority {
    CRITICAL("CRITICAL"),
    HIGH("HIGH"),
    MEDIUM("MEDIUM"),
    LOW("LOW"),
    TRIVIAL("TRIVIAL");

    private final String value;

    TaskPriority(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static TaskPriority fromString(String priority) {
        try {
            return TaskPriority.valueOf(priority.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown priority: " + priority, e);
        }
    }
}
