package com.trymad.task_management.web.dto.user;

import java.util.Set;

import com.trymad.task_management.model.Role;

public record UserDTO(

    Long id, 
    
    String name, 

    String mail,

    Set<Role> roles) {
    
}
