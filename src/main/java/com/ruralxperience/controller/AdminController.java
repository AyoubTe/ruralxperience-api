package com.ruralxperience.controller;

import com.ruralxperience.dto.request.ModerationAction;
import com.ruralxperience.dto.response.AuditLogResponse;
import com.ruralxperience.dto.response.ExperienceSummaryResponse;
import com.ruralxperience.dto.response.PageResponse;
import com.ruralxperience.dto.response.UserResponse;
import com.ruralxperience.entity.AuditLog;
import com.ruralxperience.entity.User;
import com.ruralxperience.enums.ExperienceStatus;
import com.ruralxperience.enums.Role;
import com.ruralxperience.exception.ResourceNotFoundException;
import com.ruralxperience.mapper.UserMapper;
import com.ruralxperience.repository.AuditLogRepository;
import com.ruralxperience.repository.UserRepository;
import com.ruralxperience.service.BookingService;
import com.ruralxperience.service.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ExperienceService experienceService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuditLogRepository auditLogRepository;

    // Experience moderation
    @GetMapping("/experiences/pending")
    public PageResponse<ExperienceSummaryResponse> getPending(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        return experienceService.getByStatus(ExperienceStatus.PENDING_REVIEW, pageable);
    }

    @PostMapping("/experiences/{id}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void approve(@PathVariable Long id) {
        experienceService.approve(id);
    }

    @PostMapping("/experiences/{id}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reject(@PathVariable Long id, @RequestBody Map<String, String> body) {
        experienceService.reject(id, body.get("reason"));
    }

    @GetMapping("/moderation")
    public void moderation(@Valid ModerationAction request, @AuthenticationPrincipal User user) {

    }

    // User management
    @GetMapping("/users")
    public PageResponse<UserResponse> getUsers(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (q != null && !q.isBlank()) {
            return com.ruralxperience.dto.response.PageResponse.from(
                    userRepository.searchUsers(q, pageable).map(userMapper::toResponse));
        }
        if (role != null) {
            return com.ruralxperience.dto.response.PageResponse.from(
                    userRepository.findByRole(role, pageable).map(userMapper::toResponse));
        }
        return com.ruralxperience.dto.response.PageResponse.from(
                userRepository.findAll(pageable).map(userMapper::toResponse));
    }

    @PostMapping("/users/{id}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        user.setEnabled(false);
        userRepository.save(user);
    }

    @PostMapping("/users/{id}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enableUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        user.setEnabled(true);
        userRepository.save(user);
    }

    @GetMapping("/stats")
    public Map<String, Long> getStats() {
        return Map.of(
            "totalUsers",       userRepository.count(),
            "totalExplorers",   userRepository.countByRole(Role.EXPLORER),
            "totalHosts",       userRepository.countByRole(Role.HOST),
            "pendingExperiences", experienceService.getByStatus(ExperienceStatus.PENDING_REVIEW,
                    PageRequest.of(0, 1)).totalElements()
        );
    }

    @GetMapping("/audit")
    public PageResponse<AuditLogResponse> getAuditLog(
            @RequestParam(required = false) String action,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AuditLog> result = (action != null && !action.isBlank())
                ? auditLogRepository.findByAction(action, pageable)
                : auditLogRepository.findAll(pageable);
        return PageResponse.from(result.map(log -> new AuditLogResponse(
                log.getId(), log.getAction(), log.getEntityType(),
                log.getEntityId(), log.getAdminEmail(),
                log.getDetails(), log.getCreatedAt()
        )));
    }

    @PatchMapping("/users/{id}/role")
    public UserResponse updateUserRole(@PathVariable Long id,
                                       @RequestBody Map<String, String> body) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        user.setRole(Role.valueOf(body.get("role")));
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @PatchMapping("/users/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserStatus(@PathVariable Long id,
                                 @RequestBody Map<String, String> body) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        user.setEnabled("ACTIVE".equals(body.get("status")));
        userRepository.save(user);
    }

}
