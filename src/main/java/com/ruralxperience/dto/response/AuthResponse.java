package com.ruralxperience.dto.response;

import com.ruralxperience.enums.Role;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    Long expiresIn,
    Long userId,
    String email,
    String firstName,
    String lastName,
    Role role
) {
    public static AuthResponse of(String accessToken, String refreshToken, Long expiresIn,
                                   Long userId, String email, String firstName,
                                   String lastName, Role role) {
        return new AuthResponse(accessToken, refreshToken, "Bearer", expiresIn,
                userId, email, firstName, lastName, role);
    }
}
