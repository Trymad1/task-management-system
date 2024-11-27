package com.trymad.task_management.web.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateDTO(
    
    @NotBlank
    @Size(max = 255, message = "maximum description length is 255")
    String text) {
    
}
