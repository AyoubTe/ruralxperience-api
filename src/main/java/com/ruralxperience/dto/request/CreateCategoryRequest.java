package com.ruralxperience.dto.request;

public record CreateCategoryRequest(String name, String description, String emoji, String iconUrl, Integer sortOrder) {
}
