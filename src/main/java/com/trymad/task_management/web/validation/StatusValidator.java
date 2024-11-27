package com.trymad.task_management.web.validation;

import com.trymad.task_management.model.TaskStatus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StatusValidator implements ConstraintValidator<TaskStatusCheck, String> {

    private boolean allowNull;

    public void initialize(TaskPriorityCheck constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return allowNull == true ? true : false;
        }
        
        try {
            TaskStatus.fromString(value);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }
    
}
