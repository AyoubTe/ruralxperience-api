package com.ruralxperience.service;

import com.ruralxperience.dto.request.CreateExperienceRequest;
import com.ruralxperience.dto.response.ExperienceResponse;
import com.ruralxperience.dto.response.ExperienceSummaryResponse;
import com.ruralxperience.dto.response.HostProfileResponse;
import com.ruralxperience.dto.response.PageResponse;
import com.ruralxperience.dto.response.PhotoResponse;
import com.ruralxperience.entity.Category;
import com.ruralxperience.entity.DailyAgendaItem;
import com.ruralxperience.entity.Experience;
import com.ruralxperience.entity.ExperiencePhoto;
import com.ruralxperience.entity.HostProfile;
import com.ruralxperience.enums.ExperienceStatus;
import com.ruralxperience.exception.ForbiddenException;
import com.ruralxperience.exception.ResourceNotFoundException;
import com.ruralxperience.mapper.ExperienceMapper;
import com.ruralxperience.mapper.HostProfileMapper;
import com.ruralxperience.repository.CategoryRepository;
import com.ruralxperience.repository.ExperienceRepository;
import com.ruralxperience.repository.ExperienceSpecification;
import com.ruralxperience.repository.HostProfileRepository;
import com.ruralxperience.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final CategoryRepository categoryRepository;
    private final HostProfileRepository hostProfileRepository;
    private final ExperienceMapper experienceMapper;
    private final StorageService storageService;
    private final HostProfileMapper hostProfileMapper;
    private final PhotoRepository photoRepository;

    @Transactional(readOnly = true)
    public PageResponse<ExperienceSummaryResponse> search(
            String keyword, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice,
            Integer minDuration, Integer maxDuration, Integer minGuests, Pageable pageable) {

        Specification<Experience> spec = ExperienceSpecification.buildSearchSpec(
                keyword, categoryId, minPrice, maxPrice, minDuration, maxDuration, minGuests, null);
        Page<Experience> page = experienceRepository.findAll(spec, pageable);
        return PageResponse.from(page.map(experienceMapper::toSummary));
    }

    @Cacheable(value = "experiences", key = "#id")
    @Transactional(readOnly = true)
    public ExperienceResponse getById(Long id) {
        Experience exp = experienceRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id));
        if (exp.getStatus() != ExperienceStatus.PUBLISHED) {
            throw new ResourceNotFoundException("Experience", id);
        }
        return experienceMapper.toResponse(exp);
    }

    @Transactional(readOnly = true)
    public ExperienceResponse getByIdForHost(Long id, Long userId) {
        Experience exp = experienceRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id));
        if (!exp.getHost().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your experience");
        }
        return experienceMapper.toResponse(exp);
    }

    @Transactional
    public ExperienceResponse create(CreateExperienceRequest request, Long userId, List<MultipartFile> files) {
        HostProfile host = hostProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("HostProfile for user", userId));
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId()));

        Experience exp = experienceMapper.toEntity(request);
        exp.setHost(host);
        exp.setCategory(category);
        exp.setStatus(ExperienceStatus.DRAFT);

        if (request.agendaItems() != null) {
            request.agendaItems().forEach(a -> {
                DailyAgendaItem item = DailyAgendaItem.builder()
                        .experience(exp)
                        .dayNumber(a.dayNumber())
                        .title(a.title())
                        .description(a.description())
                        .startTime(a.startTime())
                        .endTime(a.endTime())
                        .build();
                exp.getAgendaItems().add(item);
            });
        }

        if (files != null && !files.isEmpty()) {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);

                // Upload to storage
                String imageUrl = storageService.store(file);

                ExperiencePhoto photo = ExperiencePhoto.builder()
                        .experience(exp)
                        .url(imageUrl)
                        .altText(exp.getTitle() + " - Image " + (i + 1))
                        .sortOrder(i) // Maintains the order from the UI
                        .build();

                exp.getPhotos().add(photo);

                // Automatically set the first photo as the cover photo
                if (i == 0) {
                    exp.setCoverPhotoUrl(imageUrl);
                }
            }
        }

        return experienceMapper.toResponse(experienceRepository.save(exp));
    }

    @CacheEvict(value = "experiences", key = "#id")
    @Transactional
    public ExperienceResponse update(Long id, CreateExperienceRequest request, Long userId) {
        Experience exp = experienceRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id));
        if (!exp.getHost().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your experience");
        }
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId()));

        experienceMapper.updateEntity(request, exp);
        exp.setCategory(category);
        return experienceMapper.toResponse(experienceRepository.save(exp));
    }

    @CacheEvict(value = "experiences", key = "#id")
    @Transactional
    public void submitForReview(Long id, Long userId) {
        Experience exp = getOwnedExperience(id, userId);
        if (exp.getStatus() != ExperienceStatus.DRAFT && exp.getStatus() != ExperienceStatus.REJECTED) {
            throw new IllegalStateException("Experience cannot be submitted from status: " + exp.getStatus());
        }
        exp.setStatus(ExperienceStatus.PENDING_REVIEW);
        experienceRepository.save(exp);
    }

    @Transactional(readOnly = true)
    public PageResponse<ExperienceSummaryResponse> getHostExperiences(Long userId, Pageable pageable) {
        HostProfile host = hostProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("HostProfile for user", userId));
        Page<Experience> page = experienceRepository.findByHostId(host.getId(), pageable);
        return PageResponse.from(page.map(experienceMapper::toSummary));
    }

    // Admin methods
    @Transactional(readOnly = true)
    public PageResponse<ExperienceSummaryResponse> getByStatus(ExperienceStatus status, Pageable pageable) {
        Page<Experience> page = experienceRepository.findByStatus(status, pageable);
        return PageResponse.from(page.map(experienceMapper::toSummary));
    }

    @CacheEvict(value = "experiences", key = "#id")
    @Transactional
    public void approve(Long id) {
        Experience exp = experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id));
        exp.setStatus(ExperienceStatus.PUBLISHED);
        exp.setPublishedAt(java.time.LocalDateTime.now());
        experienceRepository.save(exp);
    }

    @CacheEvict(value = "experiences", key = "#id")
    @Transactional
    public void reject(Long id, String reason) {
        Experience exp = experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id));
        exp.setStatus(ExperienceStatus.REJECTED);
        exp.setRejectionReason(reason);
        experienceRepository.save(exp);
    }

    @Transactional
    public PhotoResponse uploadPhoto(Long experienceId, MultipartFile file, Long userId) {
        Experience exp = experienceRepository.findByIdWithDetails(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", experienceId));
        if (!exp.getHost().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your experience");
        }

        // Save file and get URL (delegate to a storage service)
        String url = storageService.store(file);

        int nextOrder = exp.getPhotos().size();

        ExperiencePhoto photo = ExperiencePhoto.builder()
                .experience(exp)
                .url(url)
                .altText(file.getOriginalFilename())
                .sortOrder(nextOrder)
                .uploadedAt(LocalDateTime.now())
                .build();

        exp.getPhotos().add(photo);

        // Set cover photo if this is the first one
        if (exp.getPhotos().size() == 1) {
            exp.setCoverPhotoUrl(url);
        }

        experienceRepository.save(exp);
        return experienceMapper.toPhotoResponse(photo);
    }

    @Transactional
    public List<PhotoResponse> uploadPhotos(Long experienceId, List<MultipartFile> files, Long userId) {
        // 1. Verify experience ownership
        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found"));

        if (!experience.getHost().getId().equals(userId)) {
            throw new AccessDeniedException("You do not own this experience");
        }

        // 2. Getting current max sort order to append new photos at the end
        int currentMaxOrder = experience.getPhotos().stream()
                .mapToInt(ExperiencePhoto::getSortOrder)
                .max()
                .orElse(-1);

        // 3. Process each file
        List<ExperiencePhoto> newPhotos = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);

            // Store
            String imageUrl = storageService.store(file);

            ExperiencePhoto photo = ExperiencePhoto.builder()
                    .experience(experience)
                    .url(imageUrl)
                    .altText(experience.getTitle() + " photo " + (currentMaxOrder + i + 2))
                    .sortOrder(currentMaxOrder + i + 1)
                    .build();

            newPhotos.add(photoRepository.save(photo));
        }

        // 4. Map to DTOs
        return newPhotos.stream()
                .map(photo -> new PhotoResponse(
                        photo.getId(),
                        photo.getUrl(),
                        photo.getAltText(),
                        photo.getSortOrder(),
                        photo.getUploadedAt()))
                .toList();
    }

    @Transactional
    public void reorderPhotos(Long experienceId, List<Long> orderedIds) {
        Experience exp = experienceRepository.findByIdWithDetails(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", experienceId));

        Map<Long, ExperiencePhoto> photoMap = exp.getPhotos().stream()
                .collect(Collectors.toMap(ExperiencePhoto::getId, p -> p));

        for (int i = 0; i < orderedIds.size(); i++) {
            ExperiencePhoto photo = photoMap.get(orderedIds.get(i));
            if (photo != null) {
                photo.setSortOrder(i);
            }
        }

        // Update cover photo to be the first in the new order
        orderedIds.stream()
                .findFirst()
                .map(photoMap::get)
                .ifPresent(first -> exp.setCoverPhotoUrl(first.getUrl()));

        experienceRepository.save(exp);
    }

    @Transactional
    public void deletePhoto(Long experienceId, Long photoId, Long userId) {
        Experience exp = experienceRepository.findByIdWithDetails(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", experienceId));
        if (!exp.getHost().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your experience");
        }

        ExperiencePhoto photo = exp.getPhotos().stream()
                .filter(p -> p.getId().equals(photoId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Photo", photoId));

        // Delete from storage
        storageService.delete(photo.getUrl());

        exp.getPhotos().remove(photo);

        // Update cover photo if the deleted one was the cover
        if (photo.getUrl().equals(exp.getCoverPhotoUrl())) {
            exp.setCoverPhotoUrl(
                    exp.getPhotos().isEmpty() ? null : exp.getPhotos().get(0).getUrl()
            );
        }

        experienceRepository.save(exp);
    }

    private Experience getOwnedExperience(Long id, Long userId) {
        Experience exp = experienceRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id));
        if (!exp.getHost().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your experience");
        }
        return exp;
    }

    public void delete(Long experienceId) {
        experienceRepository.deleteById(experienceId);
    }

    @Transactional
    public ExperienceResponse updateStatus(Long id, String status, Long userId) {
        Experience exp = experienceRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id));
        if (!exp.getHost().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your experience");
        }
        exp.setStatus(ExperienceStatus.valueOf(status.toUpperCase()));
        Experience saved = experienceRepository.save(exp);
        return experienceMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public HostProfileResponse getHostExperience(Long experienceId){
        HostProfile host = experienceRepository.findHostByExperienceId(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Host for experience", experienceId));
        return hostProfileMapper.toResponse(host);
    }
}
