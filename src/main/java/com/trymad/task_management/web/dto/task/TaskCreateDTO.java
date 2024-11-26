package com.trymad.task_management.web.dto.task;

public record TaskCreateDTO(
        Long executorId,
        String title,
        String description,
        String status,
        String priority) {
}
