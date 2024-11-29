package com.trymad.task_management.service;

import org.springframework.stereotype.Service;

import com.trymad.task_management.model.Role;
import com.trymad.task_management.model.RoleEntity;
import com.trymad.task_management.repository.RolesRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@Service
public class RoleService {

    private final RolesRepository rolesRepository;

    public RoleEntity get(Role role) {
        return rolesRepository.findByName(role).orElseThrow(
                () -> new EntityNotFoundException("Role " + role + " not found"));
    }

}
