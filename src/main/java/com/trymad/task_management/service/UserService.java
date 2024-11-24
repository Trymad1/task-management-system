package com.trymad.task_management.service;

import org.springframework.stereotype.Service;

import com.trymad.task_management.model.User;
import com.trymad.task_management.repository.UserRepository;
import com.trymad.task_management.web.dto.user.UserMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor

@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public User get(Long id) {

    }

    public User create(UserCreateDTO userCreateDTO) {

    }

    public User update(UserUpdateDTO userUpdateDTO) {

    }

}
