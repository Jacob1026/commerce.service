package com.gtelant.commerce.service.services;

import com.gtelant.commerce.service.exceptions.ResourceNotFoundException;
import com.gtelant.commerce.service.models.Category;
import com.gtelant.commerce.service.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 新增分類
    @Transactional
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
    @Transactional
    public Category updateCategory(Integer id, Category categoryDetails) {
        // 1. 先撈出資料庫原本的資料 (如果找不到直接拋異常，不需要用 existsById 分開寫)
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("無法更新，找不到該分類 ID: " + id));
        existingCategory.setName(categoryDetails.getName());
        return categoryRepository.save(existingCategory);
    }

    // 刪除分類
    @Transactional
    public void deleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("無法刪除，找不到該分類 ID: " + id);
        }
        categoryRepository.deleteById(id);
    }
}