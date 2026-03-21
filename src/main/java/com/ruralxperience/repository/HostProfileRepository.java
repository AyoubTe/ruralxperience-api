package com.ruralxperience.repository;

import com.ruralxperience.entity.HostProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HostProfileRepository extends JpaRepository<HostProfile, Long> {
    Optional<HostProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
