package com.trymad.task_management.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.trymad.task_management.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Slice<Comment> findAllByTaskId(Long id, Pageable pageable);

}
