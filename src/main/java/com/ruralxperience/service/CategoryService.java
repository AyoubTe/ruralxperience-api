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
import java.util.Map;

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


    @Transactional
    public CategoryResponse createCategory(Map<String, String> request) {
        String name = request.get("name");
        String description = request.get("description");
        String emoji = request.get("emoji");
        String iconUrl = request.get("iconUrl");

        int sortOrder = 0;
        try {
            if (request.containsKey("sortOrder") && request.get("sortOrder") != null) {
                sortOrder = Integer.parseInt(request.get("sortOrder"));
            }
        } catch (NumberFormatException e) {

        }

        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new ConflictException("Category already exists: " + name);
        }

        Category cat = Category.builder()
                .name(name)
                .description(description)
                .emoji(emoji)
                .iconUrl(iconUrl)
                .sortOrder(sortOrder)
                .active(true)
                .build();

        return categoryMapper.toResponse(categoryRepository.save(cat));
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, Map<String, String> request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));

        if (request.containsKey("name") && request.get("name") != null) {
            String newName = request.get("name");
            if (!category.getName().equalsIgnoreCase(newName) && categoryRepository.existsByNameIgnoreCase(newName)) {
                throw new ConflictException("Category name already exists: " + newName);
            }
            category.setName(newName);
        }

        if (request.containsKey("description")) {
            category.setDescription(request.get("description"));
        }
        if (request.containsKey("emoji")) {
            category.setEmoji(request.get("emoji"));
        }
        if (request.containsKey("iconUrl")) {
            category.setIconUrl(request.get("iconUrl"));
        }
        if (request.containsKey("active") && request.get("active") != null) {
            category.setActive(Boolean.parseBoolean(request.get("active")));
        }
        if (request.containsKey("sortOrder") && request.get("sortOrder") != null) {
            try {
                category.setSortOrder(Integer.parseInt(request.get("sortOrder")));
            } catch (NumberFormatException e) {
                // Keep existing sort order
            }
        }

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));

        if (category.getExperiences() != null && !category.getExperiences().isEmpty()) {
            throw new ConflictException("Cannot delete category because it has existing experiences tied to it. Consider deactivating it instead.");
        }

        categoryRepository.delete(category);
    }
}
