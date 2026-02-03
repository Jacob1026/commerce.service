package com.gtelant.commerce.service.controllers;

import com.gtelant.commerce.service.dtos.CategoryResponse;
import com.gtelant.commerce.service.exceptions.ResourceNotFoundException;
import com.gtelant.commerce.service.mappers.CategoryMapper;
import com.gtelant.commerce.service.models.Category;
import com.gtelant.commerce.service.services.CategoryService;
import com.gtelant.commerce.service.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;
    @MockitoBean
    private CategoryMapper categoryMapper;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void getCategoryByIdSuccess() throws Exception {
        Category category = new Category();
        category.setId(1);
        CategoryResponse response = new CategoryResponse();
        response.setId(1);
        response.setName("Books");

        when(categoryService.getCategoryById(1)).thenReturn(category);
        when(categoryMapper.toCategoryResponse(category)).thenReturn(response);

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Books"));
    }

    @Test
    void getCategoryByIdNotFound() throws Exception {
        when(categoryService.getCategoryById(99)).thenThrow(new ResourceNotFoundException("分類不存在"));

        mockMvc.perform(get("/categories/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}