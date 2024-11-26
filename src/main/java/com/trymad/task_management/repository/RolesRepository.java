package com.trymad.task_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trymad.task_management.model.Role;
import com.trymad.task_management.model.RoleEntity;

public interface RolesRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(Role role);

}
