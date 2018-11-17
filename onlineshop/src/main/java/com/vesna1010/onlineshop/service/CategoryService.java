package com.vesna1010.onlineshop.service;

import java.util.List;

import com.vesna1010.onlineshop.model.Category;

public interface CategoryService {

	List<Category> findAllCategories();

	Category findCategoryById(Long id);

	Category saveCategory(Category category);

	void deleteCategory(Category category);

}
