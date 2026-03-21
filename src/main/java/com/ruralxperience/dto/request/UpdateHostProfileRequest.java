package com.ruralxperience.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateHostProfileRequest(
    @NotBlank @Size(min = 2, max = 100) String location,
    @Size(max = 2000) String bio,
    Double latitude,
    Double longitude,
    String websiteUrl
) {}
