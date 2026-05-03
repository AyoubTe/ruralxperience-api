package com.ruralxperience.service;

import com.ruralxperience.dto.response.CategoryResponse;
import com.ruralxperience.entity.Category;
import com.ruralxperience.exception.ConflictException;
import com.ruralxperience.exception.ResourceNotFoundException;
import com.ruralxperience.mapper.CategoryMapper;
import com.ruralxperience.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {
        return categoryRepository.findByActiveTrueOrderBySortOrderAsc()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getById(Long id) {
        return categoryMapper.toResponse(
                categoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", id)));
    }

    @Transactional
    public CategoryResponse create(String name, String description, String emoji) {
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new ConflictException("Category already exists: " + name);
        }
        Category cat = Category.builder()
                .name(name).description(description).emoji(emoji).active(true).build();
        return categoryMapper.toResponse(categoryRepository.save(cat));
    }
}
