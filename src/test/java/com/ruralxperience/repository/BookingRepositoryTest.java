package com.ruralxperience.repository;

import com.ruralxperience.entity.Booking;
import com.ruralxperience.enums.BookingStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void shouldFindByExplorerIdWithDetails() {
        // Arrange
        Long explorerId = 10L;
        Pageable pageable = PageRequest.of(0, 5);

        // Act
        Page<Booking> bookings = bookingRepository.findByExplorerIdWithDetails(explorerId, pageable);

        // Assert
        assertThat(bookings)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void shouldFindByHostIdWithDetails() {
        // Arrange
        Long hostId = 3L;
        Pageable pageable = PageRequest.of(0, 5);

        // Act
        Page<Booking> bookings = bookingRepository.findByHostIdWithDetails(hostId, pageable);

        // Assert
        assertThat(bookings)
                .isNotNull()
                .hasSize(2);
    }

    @Test
    void shouldFindConflictingBookings() {
        // Arrange
        Long experienceId = 13L;
        LocalDate startDate = LocalDate.now().plusDays(10);
        LocalDate endDate = LocalDate.now().plusDays(11);

        // Act
        List<Booking> bookings = bookingRepository.findConflictingBookings(experienceId, startDate, endDate);

        // Assert
        assertThat(bookings)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void shouldFindByIdAndExplorerId() {
        // Arrange
        Long id = 3L;
        Long explorerId = 9L;

        // Act
        Booking booking = bookingRepository.findByIdAndExplorerId(id, explorerId).orElse(null);

        // Assert
        assertThat(booking).isNotNull();
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.COMPLETED);
    }

    @Test
    void shouldFindByIdWithAllDetails() {
        // Arrange
        Long id = 13L;

        // Act
        Booking booking = bookingRepository.findByIdWithAllDetails(id).orElse(null);

        // Assert
        assertThat(booking).isNull();
    }

    @Test
    void shouldCountByStatus() {
        // Arrange
        BookingStatus status = BookingStatus.COMPLETED;

        // Act
        Long count = bookingRepository.countByStatus(status);

        assertThat(count).isEqualTo(3);
    }

    @Test
    void shouldCountPendingByHostId() {
        // Arrange
        Long hostId = 5L;

        // Act
        Long count =  bookingRepository.countPendingByHostId(hostId);

        // Assert
        assertThat(count).isEqualTo(1);
    }
}