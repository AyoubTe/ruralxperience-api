package com.ruralxperience.repository;

import com.ruralxperience.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldFindByActiveTrueOrderBySortOrderAsc() {
        // Arrange
        // Act
        List<Category> categories = categoryRepository.findByActiveTrueOrderBySortOrderAsc();

        // Assert
        assertThat(categories)
                .isNotEmpty()
                .hasSize(8);
    }

    @Test
    void shouldFindByNameIgnoreCase() {
        // Arrange
        String name = "RiDinG";
        // Act
        Category category = categoryRepository.findByNameIgnoreCase(name).orElse(null);

        // Assert
        assertThat(category).isNotNull();
        assertThat(category.getDescription()).isEqualTo("Horseback adventures.");
    }

    @Test
    void shouldCheckIfExistsByNameIgnoreCase() {
        // Arrange
        String name = "COOKing";

        // Act
        Boolean exist = categoryRepository.existsByNameIgnoreCase(name);

        // Assert
        assertThat(exist).isTrue();
    }
}