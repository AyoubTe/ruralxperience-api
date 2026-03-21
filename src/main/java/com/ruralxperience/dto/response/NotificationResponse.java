package com.ruralxperience.dto.response;

import com.ruralxperience.enums.NotificationType;
import java.time.LocalDateTime;

public record NotificationResponse(
    Long id,
    NotificationType type,
    String title,
    String message,
    Long relatedId,
    String relatedType,
    boolean read,
    LocalDateTime createdAt,
    LocalDateTime readAt
) {}
