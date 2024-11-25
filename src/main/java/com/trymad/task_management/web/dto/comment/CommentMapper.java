package com.trymad.task_management.web.dto.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Slice;

import java.util.List;

import com.trymad.task_management.model.Comment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(target = "authorId", source = "author.id")
    CommentDTO toDto(Comment comment);

    List<CommentDTO> toDto(Slice<Comment> comments);

    Comment toEntity(CommentDTO dto);

    Comment toEntity(CommentCreateDTO dto);

}
