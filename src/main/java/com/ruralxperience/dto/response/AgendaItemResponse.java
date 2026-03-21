package com.ruralxperience.dto.response;

public record AgendaItemResponse(Long id, Integer dayNumber, String title, String description, String startTime, String endTime) {}
