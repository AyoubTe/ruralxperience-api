package com.ruralxperience.mapper;

import com.ruralxperience.dto.request.CreateExperienceRequest;
import com.ruralxperience.dto.response.AgendaItemResponse;
import com.ruralxperience.dto.response.ExperienceResponse;
import com.ruralxperience.dto.response.ExperienceSummaryResponse;
import com.ruralxperience.dto.response.PhotoResponse;
import com.ruralxperience.entity.DailyAgendaItem;
import com.ruralxperience.entity.Experience;
import com.ruralxperience.entity.ExperiencePhoto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ExperienceMapper {

    @Mapping(target = "categoryId",    source = "category.id")
    @Mapping(target = "categoryName",  source = "category.name")
    @Mapping(target = "categoryEmoji", source = "category.emoji")
    @Mapping(target = "hostId",        source = "host.id")
    @Mapping(target = "hostFirstName", source = "host.user.firstName")
    @Mapping(target = "hostLastName",  source = "host.user.lastName")
    @Mapping(target = "hostAvatarUrl", source = "host.user.avatarUrl")
    @Mapping(target = "hostVerified",  source = "host.verified")
    ExperienceResponse toResponse(Experience experience);

    @Mapping(target = "categoryName",  source = "category.name")
    @Mapping(target = "categoryEmoji", source = "category.emoji")
    @Mapping(target = "hostFirstName", source = "host.user.firstName")
    @Mapping(target = "hostLastName",  source = "host.user.lastName")
    @Mapping(target = "hostVerified",  source = "host.verified")
    ExperienceSummaryResponse toSummary(Experience experience);

    @Mapping(target = "id",           ignore = true)
    @Mapping(target = "host",         ignore = true)
    @Mapping(target = "category",     ignore = true)
    @Mapping(target = "status",       ignore = true)
    @Mapping(target = "averageRating",ignore = true)
    @Mapping(target = "reviewCount",  ignore = true)
    @Mapping(target = "coverPhotoUrl",ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "createdAt",    ignore = true)
    @Mapping(target = "updatedAt",    ignore = true)
    @Mapping(target = "publishedAt",  ignore = true)
    @Mapping(target = "photos",       ignore = true)
    @Mapping(target = "agendaItems",  ignore = true)
    @Mapping(target = "bookings",     ignore = true)
    @Mapping(target = "wishlistItems",ignore = true)
    Experience toEntity(CreateExperienceRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",           ignore = true)
    @Mapping(target = "host",         ignore = true)
    @Mapping(target = "category",     ignore = true)
    @Mapping(target = "status",       ignore = true)
    @Mapping(target = "averageRating",ignore = true)
    @Mapping(target = "reviewCount",  ignore = true)
    @Mapping(target = "coverPhotoUrl",ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "createdAt",    ignore = true)
    @Mapping(target = "updatedAt",    ignore = true)
    @Mapping(target = "publishedAt",  ignore = true)
    @Mapping(target = "photos",       ignore = true)
    @Mapping(target = "agendaItems",  ignore = true)
    @Mapping(target = "bookings",     ignore = true)
    @Mapping(target = "wishlistItems",ignore = true)
    void updateEntity(CreateExperienceRequest request, @MappingTarget Experience experience);

    PhotoResponse toPhotoResponse(ExperiencePhoto photo);

    AgendaItemResponse toAgendaResponse(DailyAgendaItem item);
}
