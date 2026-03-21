package com.ruralxperience.dto.request;

import com.ruralxperience.enums.ActionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ModerationAction(
        @NotNull long experienceId,
        @NotNull ActionType action,
        @NotBlank String reason
) {}
