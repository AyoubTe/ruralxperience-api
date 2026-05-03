package com.ruralxperience.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruralxperience.dto.response.CategoryResponse;
import com.ruralxperience.security.JwtService;
import com.ruralxperience.security.config.SecurityConfig;
import com.ruralxperience.service.CategoryService;
import com.ruralxperience.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@Import(SecurityConfig.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    @WithMockUser(username = "test.explorer@gmail.com", roles = {"EXPLORER"})
    void shouldGetAll() throws Exception {
        // Arrange
        CategoryResponse response1 = new CategoryResponse(1L, "Farming", "Learn farm management.", "🚜", "ur1", 1, true);
        CategoryResponse response2 = new CategoryResponse(2L, "Cooking", "Traditional recipes.", "🥖", "url2", 2, true);
        CategoryResponse response3 = new CategoryResponse(3L, "Fishing", "Ocean and river fishing.", "🎣", "url3", 3, true);
        CategoryResponse response4 = new CategoryResponse(4L, "Animals", "Interact with farm animals.", "🐑", "url4", 4, true);

        when(categoryService.getAll()).thenReturn(List.of(response1, response2, response3, response4));

        // Act & Assert
        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].name").value("Cooking"));

    }

    @Test
    @WithMockUser(username = "explorer@gamil.com", roles = {"EXPLORER"})
    void getById() throws Exception {
        // Arrange
        CategoryResponse response = new CategoryResponse(1L, "Farming", "Learn farm management.", "🚜", "ur1", 1, true);
        when(categoryService.getById(1L) ).thenReturn(response);

        // Act and Assert
        mockMvc.perform(get("/api/v1/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Farming"));
    }
}