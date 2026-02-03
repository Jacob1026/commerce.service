package com.gtelant.commerce.service.controllers;

import com.gtelant.commerce.service.dtos.ApiResponse;
import com.gtelant.commerce.service.dtos.CategoryRequest;
import com.gtelant.commerce.service.dtos.CategoryResponse;
import com.gtelant.commerce.service.mappers.CategoryMapper;
import com.gtelant.commerce.service.models.Category;
import com.gtelant.commerce.service.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin("*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Category", description = "Category management APIs")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    // Create - 新增商品分類
    @Operation(summary = "新增商品分類", description = "建立一個新的商品分類")
    @PostMapping
    // 修改回傳型態為 ApiResponse<CategoryResponse>
    // 加上 @Valid 觸發 DTO 驗證
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryMapper.toCategory(categoryRequest);
        Category createdCategory = categoryService.createCategory(category);
        CategoryResponse dto = categoryMapper.toCategoryResponse(createdCategory);

        // 使用 ApiResponse.success 包裝
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(dto));
    }

    // Read - 根據 ID 查詢商品分類
    @Operation(summary = "用 ID 查詢分類", description = "取得指定 ID 的商品簡單資訊ID跟名字")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable int id) {
        Category category = categoryService.getCategoryById(id);
        // 使用 ApiResponse.success 包裝
        return ResponseEntity.ok(ApiResponse.success(categoryMapper.toCategoryResponse(category)));
    }

    // Read - 查詢所有商品分類
    @Operation(summary = "取得所有商品分類", description = "回傳一個包含所有商品分類的列表")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> responseList = categoryService.getAllCategories().stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(responseList));
    }

    // Update - 根據 ID 更新商品分類
    @Operation(summary = "更新商品分類", description = "更新指定 ID 的商品分類資訊")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable int id, @Valid @RequestBody CategoryRequest categoryRequest) {
        Category categoryToUpdate = categoryMapper.toCategory(categoryRequest);
        Category updatedCategory = categoryService.updateCategory(id, categoryToUpdate);
        return ResponseEntity.ok(ApiResponse.success(categoryMapper.toCategoryResponse(updatedCategory)));
    }

    // Delete - 根據 ID 刪除商品分類
    @Operation(summary = "刪除商品分類", description = "刪除指定 ID 的商品分類")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}