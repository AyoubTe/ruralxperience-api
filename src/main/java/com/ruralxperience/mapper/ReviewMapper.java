package com.ruralxperience.mapper;

import com.ruralxperience.dto.response.ReviewResponse;
import com.ruralxperience.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "bookingId",          source = "booking.id")
    @Mapping(target = "experienceId",       source = "experience.id")
    @Mapping(target = "experienceTitle",    source = "experience.title")
    @Mapping(target = "explorerId",         source = "explorer.id")
    @Mapping(target = "explorerFirstName",  source = "explorer.firstName")
    @Mapping(target = "explorerLastName",   source = "explorer.lastName")
    @Mapping(target = "explorerAvatarUrl",  source = "explorer.avatarUrl")
    ReviewResponse toResponse(Review review);
}
