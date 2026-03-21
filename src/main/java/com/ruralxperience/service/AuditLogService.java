package com.ruralxperience.service;

import com.ruralxperience.entity.AuditLog;
import com.ruralxperience.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Async
    public void log(Long adminId, String adminEmail, String action,
                    String entityType, Long entityId,
                    String details, String ipAddress) {
        auditLogRepository.save(AuditLog.builder()
                .adminId(adminId)
                .adminEmail(adminEmail)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .details(details)
                .ipAddress(ipAddress)
                .build());
    }
}