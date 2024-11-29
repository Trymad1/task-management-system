package com.trymad.task_management.web.dto.task;

import java.time.LocalDateTime;

import com.trymad.task_management.model.TaskPriority;
import com.trymad.task_management.model.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Default json response from server constains task")
public record TaskDTO(

        @Schema(example = "1")
        Long id,

        @Schema(example = "1")
        Long authorId,

        @Schema(example = "Oleg")
        String authorName,

        @Schema(example = "2")
        Long executorId,

        @Schema(example = "NeOleg")
        String executorName,

        @Schema(example = "Some title")
        String title, 

        @Schema(example = "Lorem Ipsum is simply dummy text")
        String description, 
        TaskStatus status,
        TaskPriority priority,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
