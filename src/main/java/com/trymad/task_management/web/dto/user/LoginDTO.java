package com.trymad.task_management.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request for /login endpoint for claim jwt token")
public record LoginDTO(
    
    @Email
    @NotBlank(message = "mail must be not blank")
    @Size(max = 60, message = "maximum mail length is 60")
    @Schema(example = "Oleg@gmail.com") 
    String mail, 
    
    @NotBlank(message = "password must be not blank")
    @Size(max = 30, message = "maximum password length is 30")
    @Schema(example = "veryveryveryverystrongpassword") 
    String password) {
    
}
