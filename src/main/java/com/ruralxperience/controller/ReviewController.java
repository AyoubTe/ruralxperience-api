package com.ruralxperience.controller;

import com.ruralxperience.dto.request.CreateReviewRequest;
import com.ruralxperience.dto.request.HostReplyRequest;
import com.ruralxperience.dto.response.PageResponse;
import com.ruralxperience.dto.response.ReviewResponse;
import com.ruralxperience.entity.User;
import com.ruralxperience.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse create(@Valid @RequestBody CreateReviewRequest request,
                                  @AuthenticationPrincipal User user) {
        return reviewService.create(request, user.getId());
    }

    @GetMapping("/experience/{experienceId}")
    public PageResponse<ReviewResponse> getForExperience(
            @PathVariable Long experienceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return reviewService.getExperienceReviews(experienceId, pageable);
    }

    @PostMapping("/{id}/reply")
    public ReviewResponse addReply(@PathVariable Long id,
                                    @Valid @RequestBody HostReplyRequest request,
                                    @AuthenticationPrincipal User user) {
        return reviewService.addHostReply(id, user.getId(), request);
    }
}
