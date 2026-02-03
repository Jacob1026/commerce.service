package com.gtelant.commerce.service.services;

import com.gtelant.commerce.service.exceptions.ResourceNotFoundException;
import com.gtelant.commerce.service.mappers.ProductMapper;
import com.gtelant.commerce.service.models.Product;
import com.gtelant.commerce.service.repositories.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("getProduct: 應在資源存在時返回 DTO")
    void getProduct_Success() {
        // Arrange
        Product product = new Product(1L, "Laptop", BigDecimal.TEN);
        ProductDTO dto = new ProductDTO(1L, "Laptop");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(dto);

        // Act
        ProductDTO result = productService.getProduct(1L);

        // Assert
        assertEquals(dto, result);
        verify(productRepository).findById(1L);
    }

    @Test
    @DisplayName("getProduct: 應在資源不存在時拋出 ResourceNotFoundException")
    void getProduct_NotFound() {
        // Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.getProduct(999L));

        // 確保 Mapper 沒有被呼叫，因為流程在 findById 後就中斷了
        verifyNoInteractions(productMapper);
    }
}