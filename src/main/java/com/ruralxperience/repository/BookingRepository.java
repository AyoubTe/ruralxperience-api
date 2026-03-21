package com.ruralxperience.repository;

import com.ruralxperience.entity.Booking;
import com.ruralxperience.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
           "JOIN FETCH b.experience e " +
           "JOIN FETCH e.host h JOIN FETCH h.user " +
           "WHERE b.explorer.id = :explorerId " +
           "ORDER BY b.createdAt DESC")
    Page<Booking> findByExplorerIdWithDetails(@Param("explorerId") Long explorerId,
                                               Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.explorer " +
            "JOIN FETCH b.experience e " +
            "JOIN FETCH e.host h " +
            "JOIN FETCH h.user u " +
            "WHERE u.id = :userId " +
            "ORDER BY b.createdAt DESC")
    Page<Booking> findByHostIdWithDetails(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.experience.id = :experienceId " +
            "AND b.status NOT IN ('CANCELLED', 'DECLINED') " +
            "AND b.startDate < :endDate AND b.endDate > :startDate")
    List<Booking> findConflictingBookings(@Param("experienceId") Long experienceId,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);

    Optional<Booking> findByIdAndExplorerId(Long id, Long explorerId);

    @Query("SELECT b FROM Booking b " +
           "JOIN FETCH b.explorer " +
           "JOIN FETCH b.experience e " +
           "JOIN FETCH e.host h " +
           "JOIN FETCH h.user " +
           "WHERE b.id = :id")
    Optional<Booking> findByIdWithAllDetails(@Param("id") Long id);

    long countByStatus(BookingStatus status);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.experience.host.id = :hostId " +
           "AND b.status = 'PENDING'")
    long countPendingByHostId(@Param("hostId") Long hostId);
}
