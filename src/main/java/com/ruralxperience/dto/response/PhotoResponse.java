package com.ruralxperience.dto.response;

import java.time.LocalDateTime;

public record PhotoResponse(Long id, String url, String altText, Integer sortOrder, LocalDateTime uploadedAt) {}
