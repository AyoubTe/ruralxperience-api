package com.ruralxperience.repository;

import com.ruralxperience.entity.HostProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HostProfileRepository extends JpaRepository<HostProfile, Long> {
    Optional<HostProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    @Query("SELECT hp FROM HostProfile hp WHERE hp.id = " +
            "(SELECT e.host.id FROM Experience e WHERE e.id = :experienceId)")
    Optional<HostProfile> findByExperienceId(@Param("experienceId") Long experienceId);
}
