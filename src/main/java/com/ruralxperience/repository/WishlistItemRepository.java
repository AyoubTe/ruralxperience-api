package com.ruralxperience.repository;

import com.ruralxperience.entity.WishlistItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    boolean existsByUserIdAndExperienceId(Long userId, Long experienceId);
    Optional<WishlistItem> findByUserIdAndExperienceId(Long userId, Long experienceId);
    Page<WishlistItem> findByUserId(Long userId, Pageable pageable);
    void deleteByUserIdAndExperienceId(Long userId, Long experienceId);
}
