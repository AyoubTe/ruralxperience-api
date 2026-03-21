package com.ruralxperience.event;

import com.ruralxperience.entity.Booking;
import com.ruralxperience.enums.BookingStatus;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BookingStatusChangedEvent extends ApplicationEvent {

    private final Booking booking;
    private final BookingStatus previousStatus;
    private final BookingStatus newStatus;

    public BookingStatusChangedEvent(Object source, Booking booking,
                                      BookingStatus previousStatus, BookingStatus newStatus) {
        super(source);
        this.booking = booking;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
    }
}
