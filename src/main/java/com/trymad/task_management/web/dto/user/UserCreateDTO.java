package com.trymad.task_management.web.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(

    @NotBlank(message = "name must not be not blank")
    @Size(max = 40, message = "maximum name length is 40")
    String name, 

    @NotBlank(message = "mail must not be not blank")
    @Size(max = 60, message = "maximum mail length is 60")
    @Email
    String mail, 

    @NotBlank(message = "password must not be not blank")
    @Size(max = 30, message = "maximum name length is 30")
    String password) {

}
