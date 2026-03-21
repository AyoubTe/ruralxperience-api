package com.ruralxperience.service;

import com.ruralxperience.dto.response.ExperienceSummaryResponse;
import com.ruralxperience.dto.response.PageResponse;
import com.ruralxperience.entity.Experience;
import com.ruralxperience.entity.User;
import com.ruralxperience.entity.WishlistItem;
import com.ruralxperience.exception.ResourceNotFoundException;
import com.ruralxperience.mapper.ExperienceMapper;
import com.ruralxperience.repository.ExperienceRepository;
import com.ruralxperience.repository.UserRepository;
import com.ruralxperience.repository.WishlistItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistItemRepository wishlistItemRepository;
    private final ExperienceRepository experienceRepository;
    private final UserRepository userRepository;
    private final ExperienceMapper experienceMapper;

    @Transactional
    public void add(Long userId, Long experienceId) {
        if (wishlistItemRepository.existsByUserIdAndExperienceId(userId, experienceId)) {
            return; // idempotent
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Experience exp = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", experienceId));
        WishlistItem item = WishlistItem.builder().user(user).experience(exp).build();
        wishlistItemRepository.save(item);
    }

    @Transactional
    public void remove(Long userId, Long experienceId) {
        wishlistItemRepository.deleteByUserIdAndExperienceId(userId, experienceId);
    }

    @Transactional(readOnly = true)
    public PageResponse<ExperienceSummaryResponse> getWishlist(Long userId, Pageable pageable) {
        Page<WishlistItem> page = wishlistItemRepository.findByUserId(userId, pageable);
        return PageResponse.from(page.map(item -> experienceMapper.toSummary(item.getExperience())));
    }

    @Transactional(readOnly = true)
    public boolean isWishlisted(Long userId, Long experienceId) {
        return wishlistItemRepository.existsByUserIdAndExperienceId(userId, experienceId);
    }
}
