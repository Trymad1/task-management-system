package com.trymad.task_management.service;

import java.text.MessageFormat;
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

    private final String NOT_FOUND_ID = "{0} with id {1} not found";
    private final String NOT_FOUND_MAIL = "{0} with mail {1} not found";

    @Transactional(readOnly = true)
    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(MessageFormat.format(NOT_FOUND_ID, "User", id)));
    }

    public User get(String mail) {
        return userRepository.findByMail(mail).orElseThrow(
            () -> new EntityNotFoundException(MessageFormat.format(NOT_FOUND_MAIL, "User", mail))
        );
    }

    public User getExistsReference(Long id) {
        if (userRepository.existsById(id))
            return userRepository.getReferenceById(id);
        throw new EntityNotFoundException(MessageFormat.format(NOT_FOUND_ID, "User", id));
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
