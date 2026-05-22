package com.taskmanager.mapper;

import com.taskmanager.dto.request.AbdrassulayevYerzatProjectCreateRequest;
import com.taskmanager.dto.response.AbdrassulayevYerzatProjectResponse;
import com.taskmanager.entity.AbdrassulayevYerzatProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AbdrassulayevYerzatProjectMapper {

    AbdrassulayevYerzatProjectMapper INSTANCE = Mappers.getMapper(AbdrassulayevYerzatProjectMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    AbdrassulayevYerzatProject toEntity(AbdrassulayevYerzatProjectCreateRequest request);

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.username", target = "ownerName")
    @Mapping(target = "taskCount", expression = "java(project.getTasks() != null ? project.getTasks().size() : 0)")
    AbdrassulayevYerzatProjectResponse toResponse(AbdrassulayevYerzatProject project);
}