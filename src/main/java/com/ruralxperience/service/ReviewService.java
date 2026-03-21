package com.ruralxperience.service;

import com.ruralxperience.dto.request.CreateReviewRequest;
import com.ruralxperience.dto.request.HostReplyRequest;
import com.ruralxperience.dto.response.PageResponse;
import com.ruralxperience.dto.response.ReviewResponse;
import com.ruralxperience.entity.Booking;
import com.ruralxperience.entity.Review;
import com.ruralxperience.entity.User;
import com.ruralxperience.exception.BadRequestException;
import com.ruralxperience.exception.ForbiddenException;
import com.ruralxperience.exception.ResourceNotFoundException;
import com.ruralxperience.mapper.ReviewMapper;
import com.ruralxperience.repository.BookingRepository;
import com.ruralxperience.repository.ExperienceRepository;
import com.ruralxperience.repository.ReviewRepository;
import com.ruralxperience.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final ExperienceRepository experienceRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponse create(CreateReviewRequest request, Long explorerId) {
        Booking booking = bookingRepository.findByIdWithAllDetails(request.bookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking", request.bookingId()));

        if (!booking.getExplorer().getId().equals(explorerId)) {
            throw new ForbiddenException("You can only review your own bookings");
        }
        if (!booking.canBeReviewed()) {
            throw new BadRequestException("Booking cannot be reviewed — must be COMPLETED and not yet reviewed");
        }

        User explorer = userRepository.findById(explorerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", explorerId));

        Review review = Review.builder()
                .booking(booking)
                .experience(booking.getExperience())
                .explorer(explorer)
                .rating(request.rating())
                .comment(request.comment())
                .visible(true)
                .build();

        Review saved = reviewRepository.save(review);
        recalculateRating(booking.getExperience().getId());
        return reviewMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PageResponse<ReviewResponse> getExperienceReviews(Long experienceId, Pageable pageable) {
        Page<Review> page = reviewRepository.findByExperienceIdAndVisibleTrue(experienceId, pageable);
        return PageResponse.from(page.map(reviewMapper::toResponse));
    }

    @Transactional
    public ReviewResponse addHostReply(Long reviewId, Long hostUserId, HostReplyRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", reviewId));
        if (!review.getExperience().getHost().getUser().getId().equals(hostUserId)) {
            throw new ForbiddenException("Not your review");
        }
        review.setHostReply(request.reply());
        review.setHostRepliedAt(LocalDateTime.now());
        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    private void recalculateRating(Long experienceId) {
        double avg = reviewRepository.findAverageRatingByExperienceId(experienceId).orElse(0.0);
        long count = reviewRepository.countVisibleByExperienceId(experienceId);
        experienceRepository.updateRating(experienceId,
                Math.round(avg * 100.0) / 100.0, (int) count);
    }
}
