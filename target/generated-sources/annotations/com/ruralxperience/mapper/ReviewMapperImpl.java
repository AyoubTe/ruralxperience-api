package com.ruralxperience.mapper;

import com.ruralxperience.dto.response.ReviewResponse;
import com.ruralxperience.entity.Booking;
import com.ruralxperience.entity.Experience;
import com.ruralxperience.entity.Review;
import com.ruralxperience.entity.User;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-28T19:15:22+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewResponse toResponse(Review review) {
        if ( review == null ) {
            return null;
        }

        Long bookingId = null;
        Long experienceId = null;
        String experienceTitle = null;
        Long explorerId = null;
        String explorerFirstName = null;
        String explorerLastName = null;
        String explorerAvatarUrl = null;
        Long id = null;
        Integer rating = null;
        String comment = null;
        String hostReply = null;
        LocalDateTime hostRepliedAt = null;
        LocalDateTime createdAt = null;

        bookingId = reviewBookingId( review );
        experienceId = reviewExperienceId( review );
        experienceTitle = reviewExperienceTitle( review );
        explorerId = reviewExplorerId( review );
        explorerFirstName = reviewExplorerFirstName( review );
        explorerLastName = reviewExplorerLastName( review );
        explorerAvatarUrl = reviewExplorerAvatarUrl( review );
        id = review.getId();
        rating = review.getRating();
        comment = review.getComment();
        hostReply = review.getHostReply();
        hostRepliedAt = review.getHostRepliedAt();
        createdAt = review.getCreatedAt();

        ReviewResponse reviewResponse = new ReviewResponse( id, bookingId, experienceId, experienceTitle, explorerId, explorerFirstName, explorerLastName, explorerAvatarUrl, rating, comment, hostReply, hostRepliedAt, createdAt );

        return reviewResponse;
    }

    private Long reviewBookingId(Review review) {
        Booking booking = review.getBooking();
        if ( booking == null ) {
            return null;
        }
        return booking.getId();
    }

    private Long reviewExperienceId(Review review) {
        Experience experience = review.getExperience();
        if ( experience == null ) {
            return null;
        }
        return experience.getId();
    }

    private String reviewExperienceTitle(Review review) {
        Experience experience = review.getExperience();
        if ( experience == null ) {
            return null;
        }
        return experience.getTitle();
    }

    private Long reviewExplorerId(Review review) {
        User explorer = review.getExplorer();
        if ( explorer == null ) {
            return null;
        }
        return explorer.getId();
    }

    private String reviewExplorerFirstName(Review review) {
        User explorer = review.getExplorer();
        if ( explorer == null ) {
            return null;
        }
        return explorer.getFirstName();
    }

    private String reviewExplorerLastName(Review review) {
        User explorer = review.getExplorer();
        if ( explorer == null ) {
            return null;
        }
        return explorer.getLastName();
    }

    private String reviewExplorerAvatarUrl(Review review) {
        User explorer = review.getExplorer();
        if ( explorer == null ) {
            return null;
        }
        return explorer.getAvatarUrl();
    }
}
