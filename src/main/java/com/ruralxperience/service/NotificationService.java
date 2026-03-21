package com.ruralxperience.service;

import com.ruralxperience.dto.response.NotificationResponse;
import com.ruralxperience.dto.response.PageResponse;
import com.ruralxperience.entity.Notification;
import com.ruralxperience.entity.User;
import com.ruralxperience.enums.NotificationType;
import com.ruralxperience.exception.ResourceNotFoundException;
import com.ruralxperience.mapper.NotificationMapper;
import com.ruralxperience.repository.NotificationRepository;
import com.ruralxperience.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Transactional
    public void create(Long userId, NotificationType type, String title, String message,
                       Long relatedId, String relatedType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Notification n = Notification.builder()
                .user(user)
                .type(type)
                .title(title)
                .message(message)
                .relatedId(relatedId)
                .relatedType(relatedType)
                .read(false)
                .build();
        notificationRepository.save(n);
    }

    @Transactional(readOnly = true)
    public PageResponse<NotificationResponse> getForUser(Long userId, Pageable pageable) {
        Page<Notification> page = notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return PageResponse.from(page.map(notificationMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }

    @Transactional
    public void markAllRead(Long userId) {
        notificationRepository.markAllReadByUserId(userId);
    }
}
