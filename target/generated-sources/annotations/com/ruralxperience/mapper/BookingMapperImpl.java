package com.ruralxperience.mapper;

import com.ruralxperience.dto.response.BookingResponse;
import com.ruralxperience.entity.Booking;
import com.ruralxperience.entity.Experience;
import com.ruralxperience.entity.User;
import com.ruralxperience.enums.BookingStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-01T18:17:27+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public BookingResponse toResponse(Booking booking) {
        if ( booking == null ) {
            return null;
        }

        Long experienceId = null;
        String experienceTitle = null;
        String experienceCoverPhoto = null;
        String experienceLocation = null;
        Long explorerId = null;
        String explorerFirstName = null;
        String explorerLastName = null;
        String explorerEmail = null;
        Long id = null;
        LocalDate startDate = null;
        LocalDate endDate = null;
        Integer numberOfGuests = null;
        BigDecimal totalPrice = null;
        BookingStatus status = null;
        String specialRequests = null;
        String hostMessage = null;
        String cancellationReason = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        experienceId = bookingExperienceId( booking );
        experienceTitle = bookingExperienceTitle( booking );
        experienceCoverPhoto = bookingExperienceCoverPhotoUrl( booking );
        experienceLocation = bookingExperienceLocation( booking );
        explorerId = bookingExplorerId( booking );
        explorerFirstName = bookingExplorerFirstName( booking );
        explorerLastName = bookingExplorerLastName( booking );
        explorerEmail = bookingExplorerEmail( booking );
        id = booking.getId();
        startDate = booking.getStartDate();
        endDate = booking.getEndDate();
        numberOfGuests = booking.getNumberOfGuests();
        totalPrice = booking.getTotalPrice();
        status = booking.getStatus();
        specialRequests = booking.getSpecialRequests();
        hostMessage = booking.getHostMessage();
        cancellationReason = booking.getCancellationReason();
        createdAt = booking.getCreatedAt();
        updatedAt = booking.getUpdatedAt();

        boolean canCancel = booking.canBeCancelledByExplorer();
        boolean canReview = booking.canBeReviewed();

        BookingResponse bookingResponse = new BookingResponse( id, experienceId, experienceTitle, experienceCoverPhoto, experienceLocation, explorerId, explorerFirstName, explorerLastName, explorerEmail, startDate, endDate, numberOfGuests, totalPrice, status, specialRequests, hostMessage, cancellationReason, canCancel, canReview, createdAt, updatedAt );

        return bookingResponse;
    }

    private Long bookingExperienceId(Booking booking) {
        Experience experience = booking.getExperience();
        if ( experience == null ) {
            return null;
        }
        return experience.getId();
    }

    private String bookingExperienceTitle(Booking booking) {
        Experience experience = booking.getExperience();
        if ( experience == null ) {
            return null;
        }
        return experience.getTitle();
    }

    private String bookingExperienceCoverPhotoUrl(Booking booking) {
        Experience experience = booking.getExperience();
        if ( experience == null ) {
            return null;
        }
        return experience.getCoverPhotoUrl();
    }

    private String bookingExperienceLocation(Booking booking) {
        Experience experience = booking.getExperience();
        if ( experience == null ) {
            return null;
        }
        return experience.getLocation();
    }

    private Long bookingExplorerId(Booking booking) {
        User explorer = booking.getExplorer();
        if ( explorer == null ) {
            return null;
        }
        return explorer.getId();
    }

    private String bookingExplorerFirstName(Booking booking) {
        User explorer = booking.getExplorer();
        if ( explorer == null ) {
            return null;
        }
        return explorer.getFirstName();
    }

    private String bookingExplorerLastName(Booking booking) {
        User explorer = booking.getExplorer();
        if ( explorer == null ) {
            return null;
        }
        return explorer.getLastName();
    }

    private String bookingExplorerEmail(Booking booking) {
        User explorer = booking.getExplorer();
        if ( explorer == null ) {
            return null;
        }
        return explorer.getEmail();
    }
}
