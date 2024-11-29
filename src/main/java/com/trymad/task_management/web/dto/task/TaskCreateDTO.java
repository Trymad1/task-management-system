package com.trymad.task_management.web.dto.task;

import com.trymad.task_management.web.validation.TaskPriorityCheck;
import com.trymad.task_management.web.validation.TaskStatusCheck;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskCreateDTO(
        
        @Schema(example = "2")
        Long executorId,

        @NotBlank(message = "title must be not blank")
        @Size(max = 24, message = "maximum title length is 24")
        @Schema(example = "Some title")
        String title,

        @NotBlank(message = "description must be not blank")
        @Size(max = 255, message = "maximum title length is 255")
        @Schema(example = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
        String description,

        @TaskStatusCheck
        @Schema(allowableValues = {"NEW", "IN_PROGRESS", "PAUSED", "CANCELED", "COMPLETED"})
        String status,
        
        @Schema(allowableValues = {"CRITICAL", "HIGH", "MEDIUM", "LOW", "TRIVIAL"})
        @TaskPriorityCheck
        String priority) {
}
