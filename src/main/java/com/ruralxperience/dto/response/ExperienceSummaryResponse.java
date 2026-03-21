package com.ruralxperience.dto.response;

import com.ruralxperience.enums.ExperienceStatus;
import java.math.BigDecimal;

public record ExperienceSummaryResponse(
    Long id,
    String title,
    String shortDescription,
    BigDecimal pricePerPerson,
    Integer durationDays,
    Integer maxGuests,
    String location,
    ExperienceStatus status,
    Double averageRating,
    Integer reviewCount,
    String coverPhotoUrl,
    String categoryName,
    String categoryEmoji,
    String hostFirstName,
    String hostLastName,
    boolean hostVerified
) {}
