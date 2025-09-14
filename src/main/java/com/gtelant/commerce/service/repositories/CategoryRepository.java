package com.gtelant.commerce.service.repositories;

import com.gtelant.commerce.service.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
