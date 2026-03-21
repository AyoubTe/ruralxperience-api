package com.ruralxperience.mapper;

import com.ruralxperience.dto.response.NotificationResponse;
import com.ruralxperience.entity.Notification;
import com.ruralxperience.enums.NotificationType;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-21T23:39:47+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationResponse toResponse(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        Long id = null;
        NotificationType type = null;
        String title = null;
        String message = null;
        Long relatedId = null;
        String relatedType = null;
        boolean read = false;
        LocalDateTime createdAt = null;
        LocalDateTime readAt = null;

        id = notification.getId();
        type = notification.getType();
        title = notification.getTitle();
        message = notification.getMessage();
        relatedId = notification.getRelatedId();
        relatedType = notification.getRelatedType();
        read = notification.isRead();
        createdAt = notification.getCreatedAt();
        readAt = notification.getReadAt();

        NotificationResponse notificationResponse = new NotificationResponse( id, type, title, message, relatedId, relatedType, read, createdAt, readAt );

        return notificationResponse;
    }
}
