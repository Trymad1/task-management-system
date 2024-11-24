package com.trymad.task_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trymad.task_management.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
