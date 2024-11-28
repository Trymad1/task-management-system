package com.trymad.task_management.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
    
    @NotBlank(message = "name must be not blank")
    @Size(max = 40, message = "maximum name length is 40")
    @Schema(example = "Oleg")
    String name, 
    
    @NotBlank(message = "mail must be not blank")
    @Size(max = 60, message = "maximum mail length is 60")
    @Email
    @Schema(example = "Oleg@gmail.com")
    String mail, 
    
    @NotBlank(message = "password must be not blank")
    @Size(max = 30, message = "maximum password length is 30")
    @Schema(example = "veryveryveryverystrongpassword")
    String password) {
    
}
