package com.ruralxperience.repository;

import com.ruralxperience.entity.ExperiencePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<ExperiencePhoto, Long> {
}
