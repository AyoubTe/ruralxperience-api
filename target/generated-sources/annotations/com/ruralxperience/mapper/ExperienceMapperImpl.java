package com.ruralxperience.mapper;

import com.ruralxperience.dto.request.CreateExperienceRequest;
import com.ruralxperience.dto.response.AgendaItemResponse;
import com.ruralxperience.dto.response.ExperienceResponse;
import com.ruralxperience.dto.response.ExperienceSummaryResponse;
import com.ruralxperience.dto.response.PhotoResponse;
import com.ruralxperience.entity.Category;
import com.ruralxperience.entity.DailyAgendaItem;
import com.ruralxperience.entity.Experience;
import com.ruralxperience.entity.ExperiencePhoto;
import com.ruralxperience.entity.HostProfile;
import com.ruralxperience.entity.User;
import com.ruralxperience.enums.ExperienceStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-21T11:49:39+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ExperienceMapperImpl implements ExperienceMapper {

    @Override
    public ExperienceResponse toResponse(Experience experience) {
        if ( experience == null ) {
            return null;
        }

        Long categoryId = null;
        String categoryName = null;
        String categoryEmoji = null;
        Long hostId = null;
        String hostFirstName = null;
        String hostLastName = null;
        String hostAvatarUrl = null;
        boolean hostVerified = false;
        Long id = null;
        String title = null;
        String shortDescription = null;
        String fullDescription = null;
        BigDecimal pricePerPerson = null;
        Integer durationDays = null;
        Integer maxGuests = null;
        String location = null;
        Double latitude = null;
        Double longitude = null;
        ExperienceStatus status = null;
        Double averageRating = null;
        Integer reviewCount = null;
        String coverPhotoUrl = null;
        List<PhotoResponse> photos = null;
        List<AgendaItemResponse> agendaItems = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        categoryId = experienceCategoryId( experience );
        categoryName = experienceCategoryName( experience );
        categoryEmoji = experienceCategoryEmoji( experience );
        hostId = experienceHostId( experience );
        hostFirstName = experienceHostUserFirstName( experience );
        hostLastName = experienceHostUserLastName( experience );
        hostAvatarUrl = experienceHostUserAvatarUrl( experience );
        hostVerified = experienceHostVerified( experience );
        id = experience.getId();
        title = experience.getTitle();
        shortDescription = experience.getShortDescription();
        fullDescription = experience.getFullDescription();
        pricePerPerson = experience.getPricePerPerson();
        durationDays = experience.getDurationDays();
        maxGuests = experience.getMaxGuests();
        location = experience.getLocation();
        latitude = experience.getLatitude();
        longitude = experience.getLongitude();
        status = experience.getStatus();
        if ( experience.getAverageRating() != null ) {
            averageRating = experience.getAverageRating().doubleValue();
        }
        reviewCount = experience.getReviewCount();
        coverPhotoUrl = experience.getCoverPhotoUrl();
        photos = experiencePhotoListToPhotoResponseList( experience.getPhotos() );
        agendaItems = dailyAgendaItemListToAgendaItemResponseList( experience.getAgendaItems() );
        createdAt = experience.getCreatedAt();
        updatedAt = experience.getUpdatedAt();

        ExperienceResponse experienceResponse = new ExperienceResponse( id, title, shortDescription, fullDescription, pricePerPerson, durationDays, maxGuests, location, latitude, longitude, status, averageRating, reviewCount, coverPhotoUrl, categoryId, categoryName, categoryEmoji, hostId, hostFirstName, hostLastName, hostAvatarUrl, hostVerified, photos, agendaItems, createdAt, updatedAt );

        return experienceResponse;
    }

    @Override
    public ExperienceSummaryResponse toSummary(Experience experience) {
        if ( experience == null ) {
            return null;
        }

        String categoryName = null;
        String categoryEmoji = null;
        String hostFirstName = null;
        String hostLastName = null;
        boolean hostVerified = false;
        Long id = null;
        String title = null;
        String shortDescription = null;
        BigDecimal pricePerPerson = null;
        Integer durationDays = null;
        Integer maxGuests = null;
        String location = null;
        ExperienceStatus status = null;
        Double averageRating = null;
        Integer reviewCount = null;
        String coverPhotoUrl = null;

        categoryName = experienceCategoryName( experience );
        categoryEmoji = experienceCategoryEmoji( experience );
        hostFirstName = experienceHostUserFirstName( experience );
        hostLastName = experienceHostUserLastName( experience );
        hostVerified = experienceHostVerified( experience );
        id = experience.getId();
        title = experience.getTitle();
        shortDescription = experience.getShortDescription();
        pricePerPerson = experience.getPricePerPerson();
        durationDays = experience.getDurationDays();
        maxGuests = experience.getMaxGuests();
        location = experience.getLocation();
        status = experience.getStatus();
        if ( experience.getAverageRating() != null ) {
            averageRating = experience.getAverageRating().doubleValue();
        }
        reviewCount = experience.getReviewCount();
        coverPhotoUrl = experience.getCoverPhotoUrl();

        ExperienceSummaryResponse experienceSummaryResponse = new ExperienceSummaryResponse( id, title, shortDescription, pricePerPerson, durationDays, maxGuests, location, status, averageRating, reviewCount, coverPhotoUrl, categoryName, categoryEmoji, hostFirstName, hostLastName, hostVerified );

        return experienceSummaryResponse;
    }

    @Override
    public Experience toEntity(CreateExperienceRequest request) {
        if ( request == null ) {
            return null;
        }

        Experience.ExperienceBuilder experience = Experience.builder();

        experience.durationDays( request.durationDays() );
        experience.fullDescription( request.fullDescription() );
        experience.latitude( request.latitude() );
        experience.location( request.location() );
        experience.longitude( request.longitude() );
        experience.maxGuests( request.maxGuests() );
        experience.pricePerPerson( request.pricePerPerson() );
        experience.shortDescription( request.shortDescription() );
        experience.title( request.title() );

        return experience.build();
    }

    @Override
    public void updateEntity(CreateExperienceRequest request, Experience experience) {
        if ( request == null ) {
            return;
        }

        if ( request.durationDays() != null ) {
            experience.setDurationDays( request.durationDays() );
        }
        if ( request.fullDescription() != null ) {
            experience.setFullDescription( request.fullDescription() );
        }
        if ( request.latitude() != null ) {
            experience.setLatitude( request.latitude() );
        }
        if ( request.location() != null ) {
            experience.setLocation( request.location() );
        }
        if ( request.longitude() != null ) {
            experience.setLongitude( request.longitude() );
        }
        if ( request.maxGuests() != null ) {
            experience.setMaxGuests( request.maxGuests() );
        }
        if ( request.pricePerPerson() != null ) {
            experience.setPricePerPerson( request.pricePerPerson() );
        }
        if ( request.shortDescription() != null ) {
            experience.setShortDescription( request.shortDescription() );
        }
        if ( request.title() != null ) {
            experience.setTitle( request.title() );
        }
    }

    @Override
    public PhotoResponse toPhotoResponse(ExperiencePhoto photo) {
        if ( photo == null ) {
            return null;
        }

        Long id = null;
        String url = null;
        String altText = null;
        Integer sortOrder = null;
        LocalDateTime uploadedAt = null;

        id = photo.getId();
        url = photo.getUrl();
        altText = photo.getAltText();
        sortOrder = photo.getSortOrder();
        uploadedAt = photo.getUploadedAt();

        PhotoResponse photoResponse = new PhotoResponse( id, url, altText, sortOrder, uploadedAt );

        return photoResponse;
    }

    @Override
    public AgendaItemResponse toAgendaResponse(DailyAgendaItem item) {
        if ( item == null ) {
            return null;
        }

        Long id = null;
        Integer dayNumber = null;
        String title = null;
        String description = null;
        String startTime = null;
        String endTime = null;

        id = item.getId();
        dayNumber = item.getDayNumber();
        title = item.getTitle();
        description = item.getDescription();
        startTime = item.getStartTime();
        endTime = item.getEndTime();

        AgendaItemResponse agendaItemResponse = new AgendaItemResponse( id, dayNumber, title, description, startTime, endTime );

        return agendaItemResponse;
    }

    private Long experienceCategoryId(Experience experience) {
        Category category = experience.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getId();
    }

    private String experienceCategoryName(Experience experience) {
        Category category = experience.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getName();
    }

    private String experienceCategoryEmoji(Experience experience) {
        Category category = experience.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getEmoji();
    }

    private Long experienceHostId(Experience experience) {
        HostProfile host = experience.getHost();
        if ( host == null ) {
            return null;
        }
        return host.getId();
    }

    private String experienceHostUserFirstName(Experience experience) {
        HostProfile host = experience.getHost();
        if ( host == null ) {
            return null;
        }
        User user = host.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getFirstName();
    }

    private String experienceHostUserLastName(Experience experience) {
        HostProfile host = experience.getHost();
        if ( host == null ) {
            return null;
        }
        User user = host.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getLastName();
    }

    private String experienceHostUserAvatarUrl(Experience experience) {
        HostProfile host = experience.getHost();
        if ( host == null ) {
            return null;
        }
        User user = host.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getAvatarUrl();
    }

    private boolean experienceHostVerified(Experience experience) {
        HostProfile host = experience.getHost();
        if ( host == null ) {
            return false;
        }
        return host.isVerified();
    }

    protected List<PhotoResponse> experiencePhotoListToPhotoResponseList(List<ExperiencePhoto> list) {
        if ( list == null ) {
            return null;
        }

        List<PhotoResponse> list1 = new ArrayList<PhotoResponse>( list.size() );
        for ( ExperiencePhoto experiencePhoto : list ) {
            list1.add( toPhotoResponse( experiencePhoto ) );
        }

        return list1;
    }

    protected List<AgendaItemResponse> dailyAgendaItemListToAgendaItemResponseList(List<DailyAgendaItem> list) {
        if ( list == null ) {
            return null;
        }

        List<AgendaItemResponse> list1 = new ArrayList<AgendaItemResponse>( list.size() );
        for ( DailyAgendaItem dailyAgendaItem : list ) {
            list1.add( toAgendaResponse( dailyAgendaItem ) );
        }

        return list1;
    }
}
