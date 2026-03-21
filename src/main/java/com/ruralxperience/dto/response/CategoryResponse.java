package com.ruralxperience.dto.response;

public record CategoryResponse(Long id, String name, String description, String emoji, String iconUrl, Integer sortOrder, boolean active) {}
