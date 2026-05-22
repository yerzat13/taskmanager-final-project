package com.taskmanager.mapper;

import com.taskmanager.dto.request.AbdrassulayevYerzatTaskCreateRequest;
import com.taskmanager.dto.request.AbdrassulayevYerzatTaskUpdateRequest;
import com.taskmanager.dto.response.AbdrassulayevYerzatTaskResponse;
import com.taskmanager.entity.AbdrassulayevYerzatTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AbdrassulayevYerzatTaskMapper {

    AbdrassulayevYerzatTaskMapper INSTANCE = Mappers.getMapper(AbdrassulayevYerzatTaskMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    AbdrassulayevYerzatTask toEntity(AbdrassulayevYerzatTaskCreateRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "projectName", source = "project.name")
    @Mapping(target = "commentCount", expression = "java(task.getComments() != null ? task.getComments().size() : 0)")
    @Mapping(target = "attachmentCount", expression = "java(task.getAttachments() != null ? task.getAttachments().size() : 0)")
    AbdrassulayevYerzatTaskResponse toResponse(AbdrassulayevYerzatTask task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    void updateEntity(@MappingTarget AbdrassulayevYerzatTask task, AbdrassulayevYerzatTaskUpdateRequest request);
}