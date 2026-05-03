package com.ruralxperience.service;

import com.ruralxperience.entity.AuditLog;
import com.ruralxperience.repository.AuditLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class AuditLogServiceTest {
    @Mock
    AuditLogRepository auditLogRepository;

    @InjectMocks
    AuditLogService auditLogService;

    @Test
    void shouldLog() {
        // Arrange
        Long adminId = 1L;
        String adminEmail = "admin@ruralxperience.com";
        String action = "APPROVE_EXPERIENCE";
        String entityType = "EXPERIENCE";
        Long  entityId = 1L;
        String details = "Approved \"A Day in the Dairy Farm\"";
        String ipAdress = "192.168.1.1";

        AuditLog auditLog = AuditLog.builder()
                .adminId(adminId)
                .adminEmail(adminEmail)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .details(details)
                .ipAddress(ipAdress)
                .build();

        // Act
        auditLogService.log(adminId, adminEmail, action, entityType, entityId, details, ipAdress);

        // Assert
        verify(auditLogRepository, times(1)).save(auditLog);
    }
}