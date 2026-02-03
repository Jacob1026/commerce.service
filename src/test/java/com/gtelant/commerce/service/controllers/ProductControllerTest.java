package com.gtelant.commerce.service.controllers;

import com.gtelant.commerce.service.dtos.ProductResponse;
import com.gtelant.commerce.service.exceptions.ResourceNotFoundException;
import com.gtelant.commerce.service.mappers.ProductImageMapper;
import com.gtelant.commerce.service.mappers.ProductMapper;
import com.gtelant.commerce.service.models.Product;
import com.gtelant.commerce.service.services.JwtService;
import com.gtelant.commerce.service.services.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;
    @MockitoBean
    private ProductMapper productMapper;
    @MockitoBean
    private ProductImageMapper productImageMapper;

    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private UserDetailsService userDetailsService;// ProductMapper 依賴它，必須 Mock

    @Test
    @DisplayName("GET /products/{id} - 成功回傳 200")
    void getProductByIdSuccess() throws Exception {
        Product product = new Product();
        product.setId(1);
        product.setName("Test Product");

        ProductResponse response = new ProductResponse();
        response.setId(1);
        response.setName("Test Product");

        when(productService.getProductById(1)).thenReturn(product);
        when(productMapper.toProductResponse(product)).thenReturn(response);

        mockMvc.perform(get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Test Product"));
    }

    @Test
    @DisplayName("GET /products/{id} - 失敗回傳 404 (測試 GlobalExceptionHandler)")
    void getProductByIdNotFound() throws Exception {
        // 模擬 Service 拋出例外
        when(productService.getProductById(999))
                .thenThrow(new ResourceNotFoundException("商品不存在"));

        mockMvc.perform(get("/products/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // 驗證 HTTP Status 404
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("商品不存在"));
    }
}