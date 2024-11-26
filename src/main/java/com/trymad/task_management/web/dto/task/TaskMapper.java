package com.trymad.task_management.web.dto.task;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Slice;

import com.trymad.task_management.model.Task;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    @Mapping(target = "status", source = "status.name")
    @Mapping(target = "priority", source = "priority.name")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "executorId", source = "executor.id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    TaskDTO toDto(Task task);

    List<TaskDTO> toDto(Slice<Task> taskDtos);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "priority", ignore = true)
    Task toEntity(TaskDTO taskDTO);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "priority", ignore = true)
    Task toEntity(TaskCreateDTO taskCreateDto);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "priority", ignore = true)
    Task toEntity(TaskUpdateDTO taskUpdateDTO);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "executor", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "title", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(TaskUpdateDTO updateDto, @MappingTarget Task task);

}
