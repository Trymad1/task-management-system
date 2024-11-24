package com.trymad.task_management.web.dto.user;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.trymad.task_management.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDTO toDto(User user);

    User toEntity(UserDTO userDTO);

    User toEntity(UserCreateDTO userCreateDTO);

    User toEntity(UserUpdateDTO userUpdateDTO);
}
