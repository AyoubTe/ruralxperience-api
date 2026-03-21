package com.ruralxperience.mapper;

import com.ruralxperience.dto.response.BookingResponse;
import com.ruralxperience.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "experienceId",       source = "experience.id")
    @Mapping(target = "experienceTitle",    source = "experience.title")
    @Mapping(target = "experienceCoverPhoto", source = "experience.coverPhotoUrl")
    @Mapping(target = "experienceLocation", source = "experience.location")
    @Mapping(target = "explorerId",         source = "explorer.id")
    @Mapping(target = "explorerFirstName",  source = "explorer.firstName")
    @Mapping(target = "explorerLastName",   source = "explorer.lastName")
    @Mapping(target = "explorerEmail",      source = "explorer.email")
    @Mapping(target = "canCancel",          expression = "java(booking.canBeCancelledByExplorer())")
    @Mapping(target = "canReview",          expression = "java(booking.canBeReviewed())")
    BookingResponse toResponse(Booking booking);
}
