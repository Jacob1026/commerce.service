package com.gtelant.commerce.service.mappers;

import com.gtelant.commerce.service.dtos.ProductRequest;
import com.gtelant.commerce.service.dtos.ProductResponse;
import com.gtelant.commerce.service.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    private final ProductImageMapper productImageMapper;

    @Autowired
    public ProductMapper(ProductImageMapper productImageMapper) {
        this.productImageMapper = productImageMapper;
    }

    /**
     * 將 Product Entity 轉換為 ProductResponse DTO
     * @param product 資料庫實體
     * @return 用於 API 回傳的 DTO
     */
    public ProductResponse toProductResponse(Product product) {
        if (product == null) {
            return null;
        }

        ProductResponse dto = new ProductResponse();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setSalePrice(product.getSalePrice());
        dto.setWidth(product.getWidth());
        dto.setHeight(product.getHeight());
        dto.setStock(product.getStock());

        // 處理關聯的 Category，將其扁平化
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }

        // 處理關聯的 ProductImage 列表
        if (product.getProductImages() != null) {
            dto.setImages(product.getProductImages().stream()
                    .map(productImageMapper::toProductImageResponse) // 使用注入的 ProductImageMapper
                    .collect(Collectors.toList()));
        } else {
            dto.setImages(Collections.emptyList()); // 如果沒有圖片，給一個空列表
        }

        return dto;
    }

    /**
     * 將 ProductRequest DTO 轉換為 Product Entity
     * @param dto 來自 API 請求的 DTO
     * @return 準備寫入資料庫的實體
     */
    public Product toProduct(ProductRequest dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();
        // 注意：這裡不設定 ID，因為是新增
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setWidth(dto.getWidth());
        product.setHeight(dto.getHeight());
        product.setStock(dto.getStock());

        // 注意：Category 的關聯通常會在 Service 層處理，而不是在 Mapper

        return product;
    }
}