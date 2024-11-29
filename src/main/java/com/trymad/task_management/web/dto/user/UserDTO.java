package com.trymad.task_management.web.dto.user;

import java.util.Set;

import com.trymad.task_management.model.Role;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Default json response from server constains user")
public record UserDTO(

        @Schema(example = "1") 
        Long id,

        @Schema(example = "Oleg") 
        String name,

        @Schema(example = "Oleg@gmail.com") 
        String mail,

        @Schema(example = "[\"ADMIN\", \"USER\"]")
        Set<Role> roles) {
}
