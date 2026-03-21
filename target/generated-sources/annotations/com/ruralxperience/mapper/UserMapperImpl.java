package com.ruralxperience.mapper;

import com.ruralxperience.dto.response.UserResponse;
import com.ruralxperience.entity.User;
import com.ruralxperience.enums.Role;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-21T11:49:39+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String email = null;
        String firstName = null;
        String lastName = null;
        String avatarUrl = null;
        String phoneNumber = null;
        Role role = null;
        boolean enabled = false;
        boolean emailVerified = false;
        LocalDateTime createdAt = null;

        id = user.getId();
        email = user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        avatarUrl = user.getAvatarUrl();
        phoneNumber = user.getPhoneNumber();
        role = user.getRole();
        enabled = user.isEnabled();
        emailVerified = user.isEmailVerified();
        createdAt = user.getCreatedAt();

        UserResponse userResponse = new UserResponse( id, email, firstName, lastName, avatarUrl, phoneNumber, role, enabled, emailVerified, createdAt );

        return userResponse;
    }
}
