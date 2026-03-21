package com.ruralxperience.dto.response;

import java.time.LocalDateTime;

public record AuditLogResponse(
        Long          id,
        String        action,
        String        entityType,
        Long          entityId,
        String        performedBy,   // ← mapped from adminEmail
        String        details,
        LocalDateTime createdAt
) {}