package com.ruralxperience.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AgendaItemRequest(
    @NotNull Integer dayNumber,
    @NotBlank String title,
    String description,
    String startTime,
    String endTime
) {}
