package com.trymad.task_management.web.dto.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.List;

import com.trymad.task_management.model.Comment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(target = "authorId", source = "author.id")
    CommentDTO toDto(Comment comment);

    List<CommentDTO> toDto(List<Comment> comments);

    Comment toEntity(CommentDTO dto);

    Comment toEntity(CommentCreateDTO dto);

}
