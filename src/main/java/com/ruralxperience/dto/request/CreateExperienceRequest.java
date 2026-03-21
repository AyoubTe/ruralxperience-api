package com.ruralxperience.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record CreateExperienceRequest(
    @NotBlank @Size(min = 5, max = 150) String title,
    @NotBlank @Size(min = 20, max = 500) String shortDescription,
    @NotBlank String fullDescription,
    @NotNull @DecimalMin("1.00") BigDecimal pricePerPerson,
    @NotNull @Min(1) @Max(30) Integer durationDays,
    @NotNull @Min(1) @Max(50) Integer maxGuests,
    @NotBlank String location,
    Double latitude,
    Double longitude,
    @NotNull Long categoryId,
    List<AgendaItemRequest> agendaItems
) {}
