package com.ruralxperience.dto.response;

import com.ruralxperience.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookingResponse(
    Long id,
    Long experienceId,
    String experienceTitle,
    String experienceCoverPhoto,
    String experienceLocation,
    Long explorerId,
    String explorerFirstName,
    String explorerLastName,
    String explorerEmail,
    LocalDate startDate,
    LocalDate endDate,
    Integer numberOfGuests,
    BigDecimal totalPrice,
    BookingStatus status,
    String specialRequests,
    String hostMessage,
    String cancellationReason,
    boolean canCancel,
    boolean canReview,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
