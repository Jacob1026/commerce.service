package com.gtelant.commerce.service.mappers;

import com.gtelant.commerce.service.dtos.CategoryRequest;
import com.gtelant.commerce.service.dtos.CategoryResponse;
import com.gtelant.commerce.service.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component // 使用 @Component 更符合 Mapper 的語意
public class CategoryMapper {

    private final ProductMapper productMapper;

    // 注入 ProductMapper 以便處理巢狀的 Product 列表
    @Autowired
    public CategoryMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    /**
     * 將 Category Entity 轉換為 CategoryResponse DTO
     * @param category 資料庫實體
     * @return 用於 API 回傳的 DTO
     */
    public CategoryResponse toCategoryResponse(Category category) {
        CategoryResponse dto = new CategoryResponse();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setDeletedAt(category.getDeletedAt());

        // 處理關聯的 Product 列表，將其轉換為 ProductResponse DTO 列表
        if (category.getProducts() != null) {
            dto.setProducts(category.getProducts().stream()
                    .map(productMapper::toProductResponse) // 使用 productMapper 進行轉換
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    /**
     * 將 CategoryRequest DTO 轉換為 Category Entity
     * @param dto 來自 API 請求的 DTO
     * @return 準備寫入資料庫的實體
     */
    public Category toCategory(CategoryRequest dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }
}