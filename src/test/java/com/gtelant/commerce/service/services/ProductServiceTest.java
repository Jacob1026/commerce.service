package com.gtelant.commerce.service.services;

import com.gtelant.commerce.service.exceptions.ResourceNotFoundException;
import com.gtelant.commerce.service.models.Category;
import com.gtelant.commerce.service.models.Product;
import com.gtelant.commerce.service.repositories.CategoryRepository;
import com.gtelant.commerce.service.repositories.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("getProductById: 成功時應回傳 Product")
    void getProduct_Success() {
        Product product = new Product();
        product.setId(1);
        product.setName("Laptop");

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1);
        assertNotNull(result);
        assertEquals("Laptop", result.getName());
    }

    @Test
    @DisplayName("getProductById: 找不到時應拋出 ResourceNotFoundException")
    void getProduct_NotFound() {
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(999));
    }

    @Test
    @DisplayName("createProduct: 分類存在時應成功儲存")
    void createProduct_Success() {
        Product product = new Product();
        Category category = new Category();
        category.setId(1);

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(product, 1);
        assertNotNull(result);
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("createProduct: 分類不存在時應拋出異常")
    void createProduct_CategoryNotFound() {
        when(categoryRepository.findById(999)).thenReturn(Optional.empty());
        Product product = new Product();

        assertThrows(ResourceNotFoundException.class, () -> productService.createProduct(product, 999));
        verify(productRepository, never()).save(any());
    }
}