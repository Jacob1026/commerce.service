package com.gtelant.commerce.service.mappers;
import com.gtelant.commerce.service.dtos.ProductImageResponse;
import com.gtelant.commerce.service.models.ProductImage;
import org.springframework.stereotype.Component;

@Component
public class ProductImageMapper {

    /**
     * 將 ProductImage Entity 轉換為 ProductImageResponse DTO
     */
    public ProductImageResponse toProductImageResponse(ProductImage productImage) {
        if (productImage == null) {
            return null;
        }
        ProductImageResponse dto = new ProductImageResponse();
        dto.setId(productImage.getId());
        dto.setImageUrl(productImage.getImageUrl());
        dto.setImageThumbnail(productImage.getImageThumbnail());
        return dto;
    }
}