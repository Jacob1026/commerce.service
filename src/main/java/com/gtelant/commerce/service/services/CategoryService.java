package com.gtelant.commerce.service.services;

import com.gtelant.commerce.service.models.Category;
import com.gtelant.commerce.service.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return categoryRepository.findById(id).orElse(null);
    }

    // 更新分類
    public Category updateCategory(Integer id, Category categoryDetails) {
        // First, check if the category with the given id exists.
        if (categoryRepository.existsById(id)) {
            // Set the ID on the new object to ensure we are updating the correct record.
            categoryDetails.setId(id);
            return categoryRepository.save(categoryDetails);
        }
        // Return null if the category was not found.
        return null;
    }

    // 刪除分類
    public boolean deleteCategory(Integer id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}