package com.trymad.task_management.web.dto.comment;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Default json response from server constains comment from task")
public record CommentDTO(

    @Schema(example = "1")
    Long authorId, 

    @Schema(example = "Oleg")
    String authorName, 

    @Schema(example = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
    String text, 
    
    LocalDateTime createdAt) {
    
}
