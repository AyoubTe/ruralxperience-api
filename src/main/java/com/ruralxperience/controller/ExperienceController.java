package com.ruralxperience.controller;

import com.ruralxperience.dto.request.CreateExperienceRequest;
import com.ruralxperience.dto.response.ExperienceResponse;
import com.ruralxperience.dto.response.ExperienceSummaryResponse;
import com.ruralxperience.dto.response.HostProfileResponse;
import com.ruralxperience.dto.response.PageResponse;
import com.ruralxperience.dto.response.PhotoResponse;
import com.ruralxperience.entity.User;
import com.ruralxperience.service.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/experiences")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    // Parse "averageRating,desc" → Sort.by(Direction.DESC, "averageRating")
    private Sort parseSort(String sortParam) {
        if (sortParam == null || sortParam.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        String[] parts = sortParam.split(",");
        String property = parts[0].trim();
        Sort.Direction direction = (parts.length > 1 && parts[1].trim().equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return Sort.by(direction, property);
    }

    @GetMapping
    public PageResponse<ExperienceSummaryResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minDuration,
            @RequestParam(required = false) Integer maxDuration,
            @RequestParam(required = false) Integer minGuests,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "averageRating") String sort,
            @RequestParam(defaultValue = "DESC") String direction) {

        // Handle "averageRating,desc" passed as a single sort param
        String sortField = sort;
        String sortDir = direction;
        if (sort.contains(",")) {
            String[] parts = sort.split(",", 2);
            sortField = parts[0].trim();
            sortDir = parts[1].trim();
        }

        // Whitelist allowed sort fields
        Set<String> allowed = Set.of("averageRating", "pricePerPerson", "createdAt", "title", "totalReviews");
        if (!allowed.contains(sortField)) {
            sortField = "createdAt";
        }

        Sort.Direction dir = Sort.Direction.fromOptionalString(sortDir).orElse(Sort.Direction.DESC);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortField));
        return experienceService.search(keyword, categoryId, minPrice, maxPrice,
                minDuration, maxDuration, minGuests, pageable);
    }

    @GetMapping("/{id}")
    public ExperienceResponse getById(@PathVariable Long id) {
        return experienceService.getById(id);
    }

    // Host endpoints
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ExperienceResponse create(@Valid @RequestBody CreateExperienceRequest request,
                                     @RequestPart("files") List<MultipartFile> files,
                                     @AuthenticationPrincipal User user) {
        return experienceService.create(request, user.getId(), files);
    }

    @PutMapping("/{id}")
    public ExperienceResponse update(@PathVariable Long id,
                                     @Valid @RequestBody CreateExperienceRequest request,
                                     @AuthenticationPrincipal User user) {
        return experienceService.update(id, request, user.getId());
    }

    @PatchMapping("/{id}/status")
    public ExperienceResponse updateStatus(@PathVariable Long id,
                                                  @RequestBody Map<String, String> body,
                                                  @AuthenticationPrincipal User user) {
        return experienceService.updateStatus(id, body.get("status"), user.getId());
    }

    @PostMapping("/{id}/submit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void submitForReview(@PathVariable Long id, @AuthenticationPrincipal User user) {
        experienceService.submitForReview(id, user.getId());
    }

    @GetMapping("/host-experiences")
    public PageResponse<ExperienceSummaryResponse> getMyExperiences(
            @AuthenticationPrincipal User user,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return experienceService.getHostExperiences(user.getId(), pageable);
    }

    @DeleteMapping("/{experienceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long experienceId, @AuthenticationPrincipal User user) {
        experienceService.delete(experienceId);
    }

    @GetMapping("/{id}/photos")
    public List<PhotoResponse> getPhotos(@PathVariable Long id) {
        return experienceService.getById(id).photos();
    }

    @PostMapping("/{id}/photo")
    @ResponseStatus(HttpStatus.CREATED)
    public PhotoResponse uploadPhoto(@PathVariable Long id,
                                     @RequestParam("file") MultipartFile file,
                                     @AuthenticationPrincipal User user) {
        return experienceService.uploadPhoto(id, file, user.getId());
    }

    @PostMapping("/{id}/photos")
    @ResponseStatus(HttpStatus.CREATED)
    public List<PhotoResponse> uploadPhotos(@PathVariable Long id,
                                            @RequestParam("files") List<MultipartFile> files,
                                            @AuthenticationPrincipal User user) {
        // We now return a List<PhotoResponse> instead of a single one
        return experienceService.uploadPhotos(id, files, user.getId());
    }

    @PutMapping("/{id}/photos/reorder")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reorderPhotos(@PathVariable Long id,
                              @RequestBody Map<String, List<Long>> body) {
        experienceService.reorderPhotos(id, body.get("orderedIds"));
    }

    @DeleteMapping("/{expId}/photos/{photoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePhoto(@PathVariable Long expId,
                            @PathVariable Long photoId,
                            @AuthenticationPrincipal User user) {
        experienceService.deletePhoto(expId, photoId, user.getId());
    }

    @GetMapping("/{experienceId}/host")
    public HostProfileResponse getHostExperience(@PathVariable Long experienceId) {
        return experienceService.getHostExperience(experienceId);
    }
}
