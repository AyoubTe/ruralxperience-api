package com.ruralxperience.dto.response;

import java.time.LocalDateTime;

public record ReviewResponse(
    Long id,
    Long bookingId,
    Long experienceId,
    String experienceTitle,
    Long explorerId,
    String explorerFirstName,
    String explorerLastName,
    String explorerAvatarUrl,
    Integer rating,
    String comment,
    String hostReply,
    LocalDateTime hostRepliedAt,
    LocalDateTime createdAt
) {}
