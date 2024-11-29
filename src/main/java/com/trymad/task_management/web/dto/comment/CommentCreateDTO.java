package com.trymad.task_management.web.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateDTO(
    
    @NotBlank
    @Size(max = 255, message = "maximum description length is 255")
    @Schema(example = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
    String text) {
    
}
