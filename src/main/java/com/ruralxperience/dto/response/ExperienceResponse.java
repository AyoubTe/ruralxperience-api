package com.ruralxperience.dto.response;

import com.ruralxperience.enums.ExperienceStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ExperienceResponse(
    Long id,
    String title,
    String shortDescription,
    String fullDescription,
    BigDecimal pricePerPerson,
    Integer durationDays,
    Integer maxGuests,
    String location,
    Double latitude,
    Double longitude,
    ExperienceStatus status,
    Double averageRating,
    Integer reviewCount,
    String coverPhotoUrl,
    Long categoryId,
    String categoryName,
    String categoryEmoji,
    Long hostId,
    String hostFirstName,
    String hostLastName,
    String hostAvatarUrl,
    boolean hostVerified,
    List<PhotoResponse> photos,
    List<AgendaItemResponse> agendaItems,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
