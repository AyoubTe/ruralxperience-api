package com.ruralxperience.controller;

import com.ruralxperience.dto.response.ExperienceSummaryResponse;
import com.ruralxperience.dto.response.PageResponse;
import com.ruralxperience.entity.User;
import com.ruralxperience.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public PageResponse<ExperienceSummaryResponse> getWishlist(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return wishlistService.getWishlist(user.getId(), pageable);
    }

    @PostMapping("/{experienceId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Boolean> add(@PathVariable Long experienceId,
                                    @AuthenticationPrincipal User user) {
        wishlistService.add(user.getId(), experienceId);
        return Map.of("wishlisted", true);
    }

    @DeleteMapping("/{experienceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long experienceId, @AuthenticationPrincipal User user) {
        wishlistService.remove(user.getId(), experienceId);
    }

    @GetMapping("/{experienceId}/status")
    public Map<String, Boolean> status(@PathVariable Long experienceId,
                                       @AuthenticationPrincipal User user) {
        return Map.of("wishlisted", wishlistService.isWishlisted(user.getId(), experienceId));
    }
}
