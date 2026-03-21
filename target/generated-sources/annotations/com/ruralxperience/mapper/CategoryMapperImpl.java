package com.ruralxperience.mapper;

import com.ruralxperience.dto.response.CategoryResponse;
import com.ruralxperience.entity.Category;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-21T23:39:47+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryResponse toResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String description = null;
        String emoji = null;
        String iconUrl = null;
        Integer sortOrder = null;
        boolean active = false;

        id = category.getId();
        name = category.getName();
        description = category.getDescription();
        emoji = category.getEmoji();
        iconUrl = category.getIconUrl();
        sortOrder = category.getSortOrder();
        active = category.isActive();

        CategoryResponse categoryResponse = new CategoryResponse( id, name, description, emoji, iconUrl, sortOrder, active );

        return categoryResponse;
    }
}
