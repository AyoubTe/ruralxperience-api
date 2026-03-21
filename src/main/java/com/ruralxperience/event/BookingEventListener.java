package com.ruralxperience.event;

import com.ruralxperience.entity.Booking;
import com.ruralxperience.enums.BookingStatus;
import com.ruralxperience.enums.NotificationType;
import com.ruralxperience.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventListener {

    private final NotificationService notificationService;

    @Async
    @EventListener
    public void handleBookingStatusChanged(BookingStatusChangedEvent event) {
        Booking booking = event.getBooking();
        BookingStatus newStatus = event.getNewStatus();

        try {
            switch (newStatus) {
                case PENDING -> {
                    // Notify host of new booking request
                    Long hostUserId = booking.getExperience().getHost().getUser().getId();
                    notificationService.create(
                            hostUserId,
                            NotificationType.BOOKING_REQUEST,
                            "New booking request",
                            booking.getExplorer().getFullName() + " wants to book " +
                                    booking.getExperience().getTitle() + " ",
                            booking.getId(), "BOOKING");
                }
                case CONFIRMED -> {
                    notificationService.create(
                            booking.getExplorer().getId(),
                            NotificationType.BOOKING_CONFIRMED,
                            "Booking confirmed!",
                            "Your booking for " + booking.getExperience().getTitle() +
                                    " has been confirmed.",
                            booking.getId(), "BOOKING");
                }
                case DECLINED -> {
                    notificationService.create(
                            booking.getExplorer().getId(),
                            NotificationType.BOOKING_DECLINED,
                            "Booking declined",
                            "Unfortunately your booking for " +
                                    booking.getExperience().getTitle() + " was declined.",
                            booking.getId(), "BOOKING");
                }
                case CANCELLED -> {
                    Long hostUserId = booking.getExperience().getHost().getUser().getId();
                    notificationService.create(
                            hostUserId,
                            NotificationType.BOOKING_CANCELLED,
                            "Booking cancelled",
                            booking.getExplorer().getFullName() + " cancelled their booking for " +
                                    booking.getExperience().getTitle() + ".",
                            booking.getId(), "BOOKING");
                }
                case COMPLETED -> {
                    notificationService.create(
                            booking.getExplorer().getId(),
                            NotificationType.BOOKING_COMPLETED,
                            "How was your experience?",
                            "Your experience " + booking.getExperience().getTitle() +
                                    "is complete. Leave a review!",
                            booking.getId(), "BOOKING");
                }
            }
        } catch (Exception e) {
            log.error("Failed to create notification for booking {}: {}",
                    booking.getId(), e.getMessage());
        }
    }
}
