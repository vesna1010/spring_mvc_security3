package com.vesna1010.onlineshop.service;

import java.util.List;

import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.model.Product;

public interface ProductService {

	List<Product> findProductsByPage(Integer currentPage, Integer pageSize);

	List<Product> findProductsByCategoryAndPage(Category category, Integer currentPage, Integer pageSize);

	Product findProductById(String id);

	Product saveProduct(Product product);

	void deleteProduct(Product product);

	long countAllProducts();

	long countProductsByCategory(Category category);

}

