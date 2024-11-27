package com.trymad.task_management.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trymad.task_management.model.Comment;
import com.trymad.task_management.model.Role;
import com.trymad.task_management.model.Task;
import com.trymad.task_management.model.User;
import com.trymad.task_management.repository.CommentRepository;
import com.trymad.task_management.web.dto.comment.CommentCreateDTO;
import com.trymad.task_management.web.dto.comment.CommentMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;
    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    public Slice<Comment> getByTaskId(Long id,
            @PageableDefault(page = 0, size = 20, sort = "createdAt") Pageable pageable) {
        taskService.existOrThrow(id);
        final Slice<Comment> slice = commentRepository.findAllByTaskId(id, pageable);
        return slice;
    }

    public Comment addComment(CommentCreateDTO createDto, Long taskId) throws AccessDeniedException {
        final Comment comment = commentMapper.toEntity(createDto);
        final Task task = taskService.get(taskId);
        checkPermissions(task);

        final User user = userService.get(userService.getCurrentUser().getUsername());
        comment.setTask(task);
        comment.setAuthor(user);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    private void checkPermissions(Task task) throws AccessDeniedException {
        final UserDetails currentUser = userService.getCurrentUser();
        final Set<Role> roles = userService.mapToRoles(currentUser.getAuthorities());
        if (roles.contains(Role.ADMIN)) {
            return;
        }

        if (task.getExecutor() == null || !task.getExecutor().getMail().equals(currentUser.getUsername())) {
            throw new AccessDeniedException("You can`t comment this task");
        }
    }

}
