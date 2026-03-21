package com.ruralxperience.dto.response;

import com.ruralxperience.enums.Role;
import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String email,
    String firstName,
    String lastName,
    String avatarUrl,
    String phoneNumber,
    Role role,
    boolean enabled,
    boolean emailVerified,
    LocalDateTime createdAt
) {}
