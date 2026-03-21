package com.ruralxperience.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HostProfileResponse(
    Long id,
    Long userId,
    String firstName,
    String lastName,
    String avatarUrl,
    String bio,
    String location,
    Double latitude,
    Double longitude,
    boolean verified,
    BigDecimal totalEarnings,
    String websiteUrl,
    LocalDateTime createdAt
) {}
