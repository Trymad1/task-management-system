package com.trymad.task_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trymad.task_management.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTaskId(Long id);
    
}
