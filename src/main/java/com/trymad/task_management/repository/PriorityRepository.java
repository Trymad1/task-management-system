package com.trymad.task_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trymad.task_management.model.TaskPriority;

public interface PriorityRepository extends JpaRepository<TaskPriority, Long> {
    
    Optional<TaskPriority> findByValue(String name);
}
