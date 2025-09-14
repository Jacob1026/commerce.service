package com.gtelant.commerce.service.controllers;

import com.gtelant.commerce.service.dtos.CategoryRequest;
import com.gtelant.commerce.service.dtos.CategoryResponse;
import com.gtelant.commerce.service.mappers.CategoryMapper;
import com.gtelant.commerce.service.models.Category;
import com.gtelant.commerce.service.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories") // API 的基礎路徑
@CrossOrigin("*")
@Tag(name = "Category", description = "Category management APIs")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    /**
     * Create - 新增商品分類
     */
    @Operation(summary = "新增商品分類", description = "建立一個新的商品分類")
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
        Category category = categoryMapper.toCategory(categoryRequest);
        Category createdCategory = categoryService.createCategory(category);
        CategoryResponse dto = categoryMapper.toCategoryResponse(createdCategory);
        return ResponseEntity.ok(dto);
    }

    /**
     * Read - 根據 ID 查詢商品分類
     */
    @Operation(summary = "用 ID 查詢分類", description = "取得指定 ID 的商品分類詳細資訊")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable int id) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        CategoryResponse dto = categoryMapper.toCategoryResponse(category);
        return ResponseEntity.ok(dto);
    }

    /**
     * Read - 查詢所有商品分類
     */
    @Operation(summary = "取得所有商品分類", description = "回傳一個包含所有商品分類的列表")
    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories().stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    /**
     * Update - 根據 ID 更新商品分類
     */
    @Operation(summary = "更新商品分類", description = "更新指定 ID 的商品分類資訊")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable int id, @RequestBody CategoryRequest categoryRequest) {
        Category categoryToUpdate = categoryMapper.toCategory(categoryRequest);
        Category updatedCategory = categoryService.updateCategory(id, categoryToUpdate);
        if (updatedCategory == null) {
            return ResponseEntity.notFound().build();
        }
        CategoryResponse dto = categoryMapper.toCategoryResponse(updatedCategory);
        return ResponseEntity.ok(dto);
    }

    /**
     * Delete - 根據 ID 刪除商品分類
     */
    @Operation(summary = "刪除商品分類", description = "刪除指定 ID 的商品分類")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (!deleted) {
            return ResponseEntity.notFound().build(); // 找不到該資源，無法刪除
        }
        return ResponseEntity.noContent().build(); // 成功刪除，回傳 204 No Content
    }
}