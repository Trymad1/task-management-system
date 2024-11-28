package com.trymad.task_management.web.dto.task;

import java.time.LocalDateTime;

import com.trymad.task_management.model.TaskPriority;
import com.trymad.task_management.model.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Default json response from server constains task")
public record TaskDTO(
        Long id,
        Long authorId,
        String authorName,
        Long executorId,
        String executorName,
        String title, 
        String description, 
        TaskStatus status,
        TaskPriority priority,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
