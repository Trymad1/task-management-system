package com.trymad.task_management.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trymad.task_management.model.User;
import com.trymad.task_management.repository.UserRepository;
import com.trymad.task_management.web.dto.user.UserCreateDTO;
import com.trymad.task_management.web.dto.user.UserMapper;
import com.trymad.task_management.web.dto.user.UserUpdateDTO;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    public User create(UserCreateDTO userCreateDTO) {
        final User user = userMapper.toEntity(userCreateDTO);
        final LocalDateTime now = LocalDateTime.now();

        user.setCreated_at(now);
        user.setUpdated_at(now);

        return userRepository.save(user);
    }

    public User update(UserUpdateDTO userUpdateDTO, Long id) {
        final User user = this.get(id);

        user.setPassword(userUpdateDTO.password());
        user.setMail(userUpdateDTO.mail());
        user.setName(userUpdateDTO.name());
        user.setUpdated_at(LocalDateTime.now());

        return userRepository.save(user);
    }

}
