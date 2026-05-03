package com.ruralxperience.service;

import com.ruralxperience.dto.request.CreateBookingRequest;
import com.ruralxperience.dto.response.BookingResponse;
import com.ruralxperience.dto.response.PageResponse;
import com.ruralxperience.entity.Booking;
import com.ruralxperience.entity.Experience;
import com.ruralxperience.entity.User;
import com.ruralxperience.enums.BookingStatus;
import com.ruralxperience.enums.ExperienceStatus;
import com.ruralxperience.event.BookingStatusChangedEvent;
import com.ruralxperience.exception.BadRequestException;
import com.ruralxperience.exception.ForbiddenException;
import com.ruralxperience.exception.ResourceNotFoundException;
import com.ruralxperience.mapper.BookingMapper;
import com.ruralxperience.repository.BookingRepository;
import com.ruralxperience.repository.ExperienceRepository;
import com.ruralxperience.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ExperienceRepository experienceRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public BookingResponse create(CreateBookingRequest request, Long explorerId) {
        Experience exp = experienceRepository.findByIdWithDetails(request.experienceId())
                .orElseThrow(() -> new ResourceNotFoundException("Experience", request.experienceId()));

        if (exp.getStatus() != ExperienceStatus.PUBLISHED) {
            throw new BadRequestException("Experience is not available for booking");
        }
        if (request.numberOfGuests() > exp.getMaxGuests()) {
            throw new BadRequestException("Number of guests exceeds maximum capacity of " + exp.getMaxGuests());
        }
        if (!request.endDate().isAfter(request.startDate())) {
            throw new BadRequestException("End date must be after start date");
        }

        boolean conflict = !bookingRepository.findConflictingBookings(
                exp.getId(), request.startDate(), request.endDate()).isEmpty();
        if (conflict) {
            throw new BadRequestException("Experience is not available for the selected dates");
        }

        User explorer = userRepository.findById(explorerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", explorerId));

        BigDecimal totalPrice = exp.getPricePerPerson()
                .multiply(BigDecimal.valueOf(request.numberOfGuests()));

        Booking booking = Booking.builder()
                .explorer(explorer)
                .experience(exp)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .numberOfGuests(request.numberOfGuests())
                .totalPrice(totalPrice)
                .specialRequests(request.specialRequests())
                .status(BookingStatus.PENDING)
                .build();

        Booking saved = bookingRepository.save(booking);

        eventPublisher.publishEvent(new BookingStatusChangedEvent(
                this, saved, null, BookingStatus.PENDING));

        return bookingMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PageResponse<BookingResponse> getExplorerBookings(Long explorerId, Pageable pageable) {
        Page<Booking> page = bookingRepository.findByExplorerIdWithDetails(explorerId, pageable);
        return PageResponse.from(page.map(bookingMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public PageResponse<BookingResponse> getHostBookings(Long userId, Pageable pageable) {
        Page<Booking> page = bookingRepository.findByHostIdWithDetails(userId, pageable);
        return PageResponse.from(page.map(bookingMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public BookingResponse getById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findByIdWithAllDetails(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", bookingId));
        boolean isExplorer = booking.getExplorer().getId().equals(userId);
        boolean isHost = booking.getExperience().getHost().getUser().getId().equals(userId);
        if (!isExplorer && !isHost) {
            throw new ForbiddenException("Access denied");
        }
        return bookingMapper.toResponse(booking);
    }

    @Transactional
    public BookingResponse confirm(Long bookingId, Long hostUserId, String hostMessage) {
        Booking booking = getHostOwnedBooking(bookingId, hostUserId);
        assertStatus(booking, BookingStatus.PENDING, "confirm");
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setConfirmedAt(LocalDateTime.now());
        booking.setHostMessage(hostMessage);
        Booking saved = bookingRepository.save(booking);
        eventPublisher.publishEvent(new BookingStatusChangedEvent(
                this, saved, BookingStatus.PENDING, BookingStatus.CONFIRMED));
        return bookingMapper.toResponse(saved);
    }

    @Transactional
    public BookingResponse decline(Long bookingId, Long hostUserId, String reason) {
        Booking booking = getHostOwnedBooking(bookingId, hostUserId);
        assertStatus(booking, BookingStatus.PENDING, "decline");
        booking.setStatus(BookingStatus.DECLINED);
        booking.setHostMessage(reason);
        Booking saved = bookingRepository.save(booking);
        eventPublisher.publishEvent(new BookingStatusChangedEvent(
                this, saved, BookingStatus.PENDING, BookingStatus.DECLINED));
        return bookingMapper.toResponse(saved);
    }

    @Transactional
    public BookingResponse cancel(Long bookingId, Long explorerId, String reason) {
        Booking booking = bookingRepository.findByIdAndExplorerId(bookingId, explorerId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", bookingId));
        if (!booking.canBeCancelledByExplorer()) {
            throw new BadRequestException("Booking cannot be cancelled in status: " + booking.getStatus());
        }
        BookingStatus previous = booking.getStatus();
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(reason);
        Booking saved = bookingRepository.save(booking);
        eventPublisher.publishEvent(new BookingStatusChangedEvent(
                this, saved, previous, BookingStatus.CANCELLED));
        return bookingMapper.toResponse(saved);
    }

    @Transactional
    public BookingResponse complete(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", bookingId));
        assertStatus(booking, BookingStatus.CONFIRMED, "complete");
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setCompletedAt(LocalDateTime.now());
        Booking saved = bookingRepository.save(booking);
        eventPublisher.publishEvent(new BookingStatusChangedEvent(
                this, saved, BookingStatus.CONFIRMED, BookingStatus.COMPLETED));
        return bookingMapper.toResponse(saved);
    }

    private Booking getHostOwnedBooking(Long bookingId, Long hostUserId) {
        Booking booking = bookingRepository.findByIdWithAllDetails(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", bookingId));
        if (!booking.getExperience().getHost().getUser().getId().equals(hostUserId)) {
            throw new ForbiddenException("Not your booking");
        }
        return booking;
    }

    private void assertStatus(Booking booking, BookingStatus required, String action) {
        if (booking.getStatus() != required) {
            throw new BadRequestException(
                    "Cannot " + action + " booking in status: " + booking.getStatus());
        }
    }
}
