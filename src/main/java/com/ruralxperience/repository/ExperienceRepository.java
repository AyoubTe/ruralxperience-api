package com.ruralxperience.repository;

import com.ruralxperience.entity.Experience;
import com.ruralxperience.enums.ExperienceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience, Long>,
        JpaSpecificationExecutor<Experience> {

    Page<Experience> findByStatus(ExperienceStatus status, Pageable pageable);

    @Query("SELECT e FROM Experience e " +
           "JOIN FETCH e.host h JOIN FETCH h.user u " +
           "JOIN FETCH e.category c " +
           "WHERE e.id = :id")
    Optional<Experience> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT e FROM Experience e WHERE e.host.id = :hostId")
    Page<Experience> findByHostId(@Param("hostId") Long hostId, Pageable pageable);

    @Query("SELECT e FROM Experience e WHERE e.host.id = :hostId AND e.status = :status")
    Page<Experience> findByHostIdAndStatus(@Param("hostId") Long hostId,
                                            @Param("status") ExperienceStatus status,
                                            Pageable pageable);

    @Modifying
    @Query("UPDATE Experience e SET e.averageRating = :rating, " +
           "e.reviewCount = :count WHERE e.id = :id")
    void updateRating(@Param("id") Long id,
                      @Param("rating") Double rating,
                      @Param("count") Integer count);

    long countByStatus(ExperienceStatus status);
}
