package com.ruralxperience.repository;

import com.ruralxperience.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByExperienceIdAndVisibleTrue(Long experienceId, Pageable pageable);

    boolean existsByBookingId(Long bookingId);

    Optional<Review> findByBookingId(Long bookingId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.experience.id = :expId AND r.visible = true")
    Optional<Double> findAverageRatingByExperienceId(@Param("expId") Long experienceId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.experience.id = :expId AND r.visible = true")
    long countVisibleByExperienceId(@Param("expId") Long experienceId);

    Page<Review> findByExplorerId(Long explorerId, Pageable pageable);
}
