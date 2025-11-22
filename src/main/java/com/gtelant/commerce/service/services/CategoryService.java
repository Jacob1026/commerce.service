package com.gtelant.commerce.service.services;

import com.gtelant.commerce.service.exceptions.ResourceNotFoundException; // 記得 import 你的異常類
import com.gtelant.commerce.service.models.Category;
import com.gtelant.commerce.service.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 新增分類
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    // 取得所有分類
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 用 ID 尋找分類
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("找不到該分類 ID: " + id));
    }

    // 更新分類
    public Category updateCategory(Integer id, Category categoryDetails) {
        // 1. 檢查 ID 是否存在，不存在直接拋出異常
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("無法更新，找不到該分類 ID: " + id);
        }

        // 2. 設定 ID 並存檔 (直接覆蓋)
        categoryDetails.setId(id);
        return categoryRepository.save(categoryDetails);
    }

    // 刪除分類
    public void deleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("無法刪除，找不到該分類 ID: " + id);
        }
        categoryRepository.deleteById(id);
    }
}