package com.trymad.task_management.web.dto.comment;

import java.time.LocalDateTime;

public record CommentDTO(Long authorId, String text, LocalDateTime createdAt) {
    
}
