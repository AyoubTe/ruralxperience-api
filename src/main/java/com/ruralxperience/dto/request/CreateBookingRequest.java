package com.ruralxperience.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CreateBookingRequest(
    @NotNull Long experienceId,
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate,
    @NotNull @Positive Integer numberOfGuests,
    @Size(max = 1000) String specialRequests
) {}
