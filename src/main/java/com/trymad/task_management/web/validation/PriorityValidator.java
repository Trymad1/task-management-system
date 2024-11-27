package com.trymad.task_management.web.validation;

import com.trymad.task_management.model.TaskPriority;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PriorityValidator implements ConstraintValidator<TaskPriorityCheck, String> {

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
            TaskPriority.fromString(value);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
    
}
