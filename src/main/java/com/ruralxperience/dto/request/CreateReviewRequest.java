package com.ruralxperience.dto.request;

import jakarta.validation.constraints.*;

public record CreateReviewRequest(
    @NotNull Long bookingId,
    @NotNull @Min(1) @Max(5) Integer rating,
    @Size(min = 10, max = 2000) String comment
) {}
