package com.trymad.task_management.web.dto.comment;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Default json response from server constains comment from task")
public record CommentDTO(Long authorId, String authorName, String text, LocalDateTime createdAt) {
    
}
