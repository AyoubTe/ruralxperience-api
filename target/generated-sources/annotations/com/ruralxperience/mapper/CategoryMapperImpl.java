package com.ruralxperience.mapper;

import com.ruralxperience.dto.response.CategoryResponse;
import com.ruralxperience.entity.Category;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-01T18:17:27+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
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
