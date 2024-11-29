package com.trymad.task_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trymad.task_management.model.TaskStatus;
import com.trymad.task_management.model.TaskStatusEntity;

public interface StatusRepository extends JpaRepository<TaskStatusEntity, Long> {
    
    Optional<TaskStatusEntity> findByName(TaskStatus status);

}
