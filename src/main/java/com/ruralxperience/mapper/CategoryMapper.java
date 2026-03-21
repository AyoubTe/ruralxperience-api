package com.ruralxperience.mapper;

import com.ruralxperience.dto.response.CategoryResponse;
import com.ruralxperience.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);
}
