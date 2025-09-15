package com.gtelant.commerce.service.mappers;

import com.gtelant.commerce.service.dtos.CategoryRequest;
import com.gtelant.commerce.service.dtos.CategoryResponse;
import com.gtelant.commerce.service.dtos.ProductSimpleResponse;
import com.gtelant.commerce.service.models.Category;
import com.gtelant.commerce.service.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    // 注入 ProductMapper
    //可以把 ProductMapper 抽離讓 CategoryMapper保持單一職責，避免過度耦合，循環注入
//    private final ProductMapper productMapper;
//    @Autowired
//    public CategoryMapper(ProductMapper productMapper) {
//        this.productMapper = productMapper;
//    }


    public CategoryResponse toCategoryResponse(Category category) {
        CategoryResponse dto = new CategoryResponse();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setDeletedAt(category.getDeletedAt());

        // 處理關聯的 Product 列表，將其轉換為 ProductResponse DTO 列表
        if(category.getProducts() == null){
            dto.setProducts(Collections.emptyList());
        } else {
            List<ProductSimpleResponse> simpleResponse = category.getProducts().stream().map(this::toProductSimpleResponse).collect(Collectors.toList());
        }
        return dto;
    }

    private ProductSimpleResponse toProductSimpleResponse(Product product) {
        ProductSimpleResponse dto = new ProductSimpleResponse();
        dto.setId(product.getId());
        dto.setName(product.getName());
        return dto;
    }


    public Category toCategory(CategoryRequest dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }
}