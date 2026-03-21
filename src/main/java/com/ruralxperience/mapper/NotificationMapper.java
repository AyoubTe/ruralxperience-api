package com.ruralxperience.mapper;

import com.ruralxperience.dto.response.NotificationResponse;
import com.ruralxperience.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toResponse(Notification notification);
}
