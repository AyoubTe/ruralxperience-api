package com.ruralxperience.service;

import com.ruralxperience.dto.response.CategoryResponse;
import com.ruralxperience.entity.Category;
import com.ruralxperience.mapper.CategoryMapper;
import com.ruralxperience.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void shouldGetAll() {
        // Arrange
        Category category1 = new Category(1L, "Farming", "Learn farm management.", "🚜", "ur1", 1, true, new ArrayList<>());
        Category category2 = new Category(2L, "Cooking", "Traditional recipes.", "🥖", "url2", 2, true, new ArrayList<>());
        Category category3 = new Category(3L, "Fishing", "Ocean and river fishing.", "🎣", "url3", 3, true, new ArrayList<>());
        Category category4 = new Category(4L, "Animals", "Interact with farm animals.", "🐑", "url4", 4, true, new ArrayList<>());

        when(categoryRepository.findByActiveTrueOrderBySortOrderAsc()).thenReturn(List.of(category1, category2, category3, category4));

        // Act
        List<CategoryResponse> categories = categoryService.getAll();

        // Assert
        Assertions.assertNotNull(categories);
        Assertions.assertEquals(4, categories.size());
    }

    @Test
    void shouldGetById() {
        // Arrange
        Long id = 1L;
        Category category = new Category(1L, "Farming", "Learn farm management.", "🚜", "ur1", 1, true, new ArrayList<>());
        CategoryResponse response = new CategoryResponse(1L, "Farming", "Learn farm management.", "🚜", "ur1", 1, true);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponse(category)).thenReturn(response);

        // Act
        CategoryResponse result = categoryService.getById(id);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(response, result);
    }

    @Test
    void shouldCreate() {
        // Arrange
        String name = "Local Food";
        String description = "Market tours and eating.";
        String emoji = "🧀";

        Category category = new Category(1L, "Local Food", "Market tours and eating.", "🧀", "url", 1, true, new ArrayList<>());
        CategoryResponse response = new CategoryResponse(1L, "Local Food", "Market tours and eating.", "🧀", "url", 1, true);

        when(categoryRepository.existsByNameIgnoreCase(name)).thenReturn(Boolean.FALSE);
        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(response);

        // Act
        CategoryResponse result = categoryService.create(name, description, emoji);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(response, result);
    }
}