package com.ruralxperience.controller;

import com.ruralxperience.dto.response.NotificationResponse;
import com.ruralxperience.dto.response.PageResponse;
import com.ruralxperience.entity.User;
import com.ruralxperience.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public PageResponse<NotificationResponse> getMyNotifications(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationService.getForUser(user.getId(), pageable);
    }

    @GetMapping("/unread-count")
    public Map<String, Long> getUnreadCount(@AuthenticationPrincipal User user) {
        return Map.of("count", notificationService.getUnreadCount(user.getId()));
    }

    @PostMapping("/mark-all-read")
    public Map<String, String> markAllRead(@AuthenticationPrincipal User user) {
        notificationService.markAllRead(user.getId());
        return Map.of("status", "ok");
    }
}
