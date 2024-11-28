package com.trymad.task_management.web.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO(
    
    @Email
    @NotBlank(message = "mail must be not blank")
    @Size(max = 60, message = "maximum mail length is 60")
    String mail, 
    
    @NotBlank(message = "password must be not blank")
    @Size(max = 30, message = "maximum password length is 30")
    String password) {
    
}
