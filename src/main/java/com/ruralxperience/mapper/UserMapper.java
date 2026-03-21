package com.ruralxperience.mapper;

import com.ruralxperience.dto.response.UserResponse;
import com.ruralxperience.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
