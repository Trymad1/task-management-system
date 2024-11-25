package com.trymad.task_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trymad.task_management.model.TaskPriority;
import com.trymad.task_management.model.TaskPriorityEntity;

public interface PriorityRepository extends JpaRepository<TaskPriorityEntity, Long> {
    
    Optional<TaskPriorityEntity> findByValue(TaskPriority priority);
}
