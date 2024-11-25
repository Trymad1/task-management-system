package com.trymad.task_management.service;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<Comment> getByTaskId(Long id) {
        taskService.existOrThrow(id);
        return commentRepository.findAllByTaskId(id);
    }

    public Comment addComment(CommentCreateDTO createDto, Long taskId) {
        final Comment comment = commentMapper.toEntity(createDto);

        comment.setTask(taskService.getExistsReference(taskId));
        comment.setAuthor(userService.getExistsReference(createDto.authorId()));
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }
}
