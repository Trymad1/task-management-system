package com.trymad.task_management.web.dto.task;

import java.time.LocalDateTime;

public record TaskDTO(
        Long id,
        Long authorId,
        Long executorId,
        String title, String description, 
        String status,
        String priority,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
