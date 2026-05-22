package com.taskmanager.mapper;

import com.taskmanager.dto.response.AbdrassulayevYerzatUserResponse;
import com.taskmanager.entity.AbdrassulayevYerzatUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AbdrassulayevYerzatUserMapper {

    AbdrassulayevYerzatUserMapper INSTANCE = Mappers.getMapper(AbdrassulayevYerzatUserMapper.class);

    @Mapping(source = "role", target = "role")
    AbdrassulayevYerzatUserResponse toResponse(AbdrassulayevYerzatUser user);
}