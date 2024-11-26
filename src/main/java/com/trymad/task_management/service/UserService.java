package com.trymad.task_management.service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trymad.task_management.model.Role;
import com.trymad.task_management.model.RoleEntity;
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
@Primary
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    private final String NOT_FOUND_ID = "{0} with id {1} not found";
    private final String NOT_FOUND_MAIL = "{0} with mail {1} not found";

    @Transactional(readOnly = true)
    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(MessageFormat.format(NOT_FOUND_ID, "User", id)));
    }

    public User get(String mail) {
        return userRepository.findByMail(mail).orElseThrow(
                () -> new EntityNotFoundException(MessageFormat.format(NOT_FOUND_MAIL, "User", mail)));
    }

    public boolean existsByMail(String mail) {
        return userRepository.existsByMail(mail);
    }

    public User getExistsReference(Long id) {
        if (userRepository.existsById(id))
            return userRepository.getReferenceById(id);
        throw new EntityNotFoundException(MessageFormat.format(NOT_FOUND_ID, "User", id));
    }

    public User create(UserCreateDTO userCreateDTO, Role role) {
        final User user = userMapper.toEntity(userCreateDTO);
        final LocalDateTime now = LocalDateTime.now();

        user.setPassword(passwordEncoder.encode(userCreateDTO.password()));
        user.setCreated_at(now);
        user.setUpdated_at(now);
        user.getRoles().add(roleService.get(role));

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

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        try {
            user = this.get(username);
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException("User with mail " + username + " not found");
        }

        return toUserDetails(user);
    }

    public UserDetails toUserDetails(User user) {
        final Set<Role> roles = user.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet());
        return createUserDetails(user.getMail(), user.getPassword(), roles);
    }

    private UserDetails createUserDetails(
            String mail, String password, Set<Role> roles) {
        return new org.springframework.security.core.userdetails.User(
                mail,
                password,
                roles.stream().map(role -> new SimpleGrantedAuthority(role.toString()))
                        .collect(Collectors.toList()));
    }
}
