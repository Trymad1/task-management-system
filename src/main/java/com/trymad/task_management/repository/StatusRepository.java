package com.trymad.task_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trymad.task_management.model.TaskStatus;

public interface StatusRepository extends JpaRepository<TaskStatus, Long> {
    
    Optional<TaskStatus> findByValue(String value);

}
