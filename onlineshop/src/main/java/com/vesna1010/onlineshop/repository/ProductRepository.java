package com.vesna1010.onlineshop.repository;

import java.util.List;
import java.util.Optional;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.model.Product;

public interface ProductRepository {

	List<Product> findByPage(Integer currentPage, Integer pageSize);

	List<Product> findByCategoryAndPage(Category category, Integer currentPage, Integer pageSize);

	Optional<Product> findById(String id);
	
	Product save(Product product);

	void delete(Product product);

	long count();

	long countByCategory(Category category);

}

