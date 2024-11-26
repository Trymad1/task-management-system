package com.trymad.task_management.security;

public class UserAlreadyExistsException extends RuntimeException {
    
    public UserAlreadyExistsException(String mail) {
        super("User with mail " + mail + " already exists");
    }

}
