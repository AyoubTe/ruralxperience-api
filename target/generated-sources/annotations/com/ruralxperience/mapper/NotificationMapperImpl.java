package com.ruralxperience.mapper;

import com.ruralxperience.dto.response.NotificationResponse;
import com.ruralxperience.entity.Notification;
import com.ruralxperience.enums.NotificationType;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-01T18:17:27+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
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
