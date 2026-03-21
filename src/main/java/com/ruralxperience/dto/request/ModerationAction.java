package com.ruralxperience.dto.request;

public record ModerationAction(
        Long    experienceId,
        String  action,
        String  reason
) {}
