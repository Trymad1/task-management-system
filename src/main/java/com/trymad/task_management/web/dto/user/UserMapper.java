package com.trymad.task_management.web.dto.user;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.trymad.task_management.model.Role;
import com.trymad.task_management.model.RoleEntity;
import com.trymad.task_management.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDTO toDto(User user);

    User toEntity(UserDTO userDTO);

    User toEntity(UserCreateDTO userCreateDTO);

    User toEntity(UserUpdateDTO userUpdateDTO);

    default Set<Role> mapToRoles(Set<RoleEntity> roleEntities) {
        return roleEntities.stream().map(RoleEntity::getName).collect(Collectors.toSet());
    }
}
