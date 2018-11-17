package com.vesna1010.onlineshop.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vesna1010.onlineshop.exception.CategoryCanNotDeleteException;
import com.vesna1010.onlineshop.exception.CategoryNotFoundException;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.repository.CategoryRepository;
import com.vesna1010.onlineshop.repository.ProductRepository;
import com.vesna1010.onlineshop.service.CategoryService;

@Transactional
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

	private CategoryRepository categoryRepository;
	private ProductRepository productRepository;

	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository) {
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
	}

	@Override
	public List<Category> findAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Category findCategoryById(Long id) {
		Optional<Category> optional = categoryRepository.findById(id);

		return optional.orElseThrow(() -> new CategoryNotFoundException("Not Found Category By Id " + id));
	}

	@Override
	public Category saveCategory(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public void deleteCategory(Category category) {
		if (productRepository.countByCategory(category) > 0) {
			throw new CategoryCanNotDeleteException("Can Not Delete Category");
		}

		categoryRepository.delete(category);
	}

}
