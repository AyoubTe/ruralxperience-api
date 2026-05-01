package com.ruralxperience.repository;

import com.ruralxperience.entity.AuditLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AuditLogRepositoryTest {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Test
    void shouldFindByAdminIdOrderByCreatedAtDesc() {
        Long  adminId = 1L;
        // 5 logs on the first page
        Pageable pageable = PageRequest.of(0, 5);

        Page<AuditLog> logs = auditLogRepository.findByAdminIdOrderByCreatedAtDesc(adminId, pageable);

        assertThat(logs)
                .isNotNull()
                .hasSize(2);
    }

    @Test
    void shouldFindByEntityTypeAndEntityIdOrderByCreatedAtDesc() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<AuditLog> logs = auditLogRepository.findByEntityTypeAndEntityIdOrderByCreatedAtDesc("EXPERIENCE",  12L,pageable);
        assertThat(logs).isEmpty();
    }

    @Test
    void shouldFindByAction() {
        // Arrange
        String action = "APPROVE_EXPERIENCE";
        Pageable pageable = PageRequest.of(0, 5);

        // Act
        Page<AuditLog> auditLog = auditLogRepository.findByAction(action, pageable);

        // Assert
        assertThat(auditLog)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void shouldFindByEntityTypeAndEntityId() {
        // Arrange
        String entityType = "EXPERIENCE";
        Long entityId = 2L;
        Pageable pageable = PageRequest.of(0, 5);

        // Act
        Page<AuditLog>  logs = auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId, pageable);

        // Assert
        assertThat(logs).isEmpty();
    }
}