package com.trymad.task_management.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import com.trymad.task_management.model.Comment;
import com.trymad.task_management.repository.CommentRepository;
import com.trymad.task_management.web.dto.comment.CommentCreateDTO;
import com.trymad.task_management.web.dto.comment.CommentMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;
    private final CommentMapper commentMapper;

    public Slice<Comment> getByTaskId(Long id, 
    @PageableDefault(page = 0, size = 20, sort = "createdAt") Pageable pageable) {
        taskService.existOrThrow(id);
        final Slice<Comment> slice = commentRepository.findAllByTaskId(id, pageable);
        return slice;
    }

    public Comment addComment(CommentCreateDTO createDto, Long taskId) {
        final Comment comment = commentMapper.toEntity(createDto);

        comment.setTask(taskService.getExistsReference(taskId));
        comment.setAuthor(userService.getExistsReference(createDto.authorId()));
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }
}
