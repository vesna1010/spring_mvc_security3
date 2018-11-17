package com.vesna1010.onlineshop.repository;

import java.util.List;
import java.util.Optional;
import com.vesna1010.onlineshop.model.Category;

public interface CategoryRepository {

	List<Category> findAll();

	Optional<Category> findById(Long id);

	Category save(Category category);

	void delete(Category category);

}
