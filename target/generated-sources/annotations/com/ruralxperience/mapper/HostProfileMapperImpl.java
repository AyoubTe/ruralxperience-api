package com.ruralxperience.mapper;

import com.ruralxperience.dto.response.HostProfileResponse;
import com.ruralxperience.entity.HostProfile;
import com.ruralxperience.entity.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-21T23:39:48+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class HostProfileMapperImpl implements HostProfileMapper {

    @Override
    public HostProfileResponse toResponse(HostProfile hostProfile) {
        if ( hostProfile == null ) {
            return null;
        }

        Long userId = null;
        String firstName = null;
        String lastName = null;
        String avatarUrl = null;
        Long id = null;
        String bio = null;
        String location = null;
        Double latitude = null;
        Double longitude = null;
        boolean verified = false;
        BigDecimal totalEarnings = null;
        String websiteUrl = null;
        LocalDateTime createdAt = null;

        userId = hostProfileUserId( hostProfile );
        firstName = hostProfileUserFirstName( hostProfile );
        lastName = hostProfileUserLastName( hostProfile );
        avatarUrl = hostProfileUserAvatarUrl( hostProfile );
        id = hostProfile.getId();
        bio = hostProfile.getBio();
        location = hostProfile.getLocation();
        latitude = hostProfile.getLatitude();
        longitude = hostProfile.getLongitude();
        verified = hostProfile.isVerified();
        totalEarnings = hostProfile.getTotalEarnings();
        websiteUrl = hostProfile.getWebsiteUrl();
        createdAt = hostProfile.getCreatedAt();

        HostProfileResponse hostProfileResponse = new HostProfileResponse( id, userId, firstName, lastName, avatarUrl, bio, location, latitude, longitude, verified, totalEarnings, websiteUrl, createdAt );

        return hostProfileResponse;
    }

    private Long hostProfileUserId(HostProfile hostProfile) {
        User user = hostProfile.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private String hostProfileUserFirstName(HostProfile hostProfile) {
        User user = hostProfile.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getFirstName();
    }

    private String hostProfileUserLastName(HostProfile hostProfile) {
        User user = hostProfile.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getLastName();
    }

    private String hostProfileUserAvatarUrl(HostProfile hostProfile) {
        User user = hostProfile.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getAvatarUrl();
    }
}
