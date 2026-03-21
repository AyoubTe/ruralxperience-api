package com.ruralxperience.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record HostReplyRequest(@NotBlank @Size(min = 10, max = 1000) String reply) {}
