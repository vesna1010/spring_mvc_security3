package com.vesna1010.onlineshop.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vesna1010.onlineshop.exception.ProductCanNotDeleteException;
import com.vesna1010.onlineshop.exception.ProductNotFoundException;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.model.Product;
import com.vesna1010.onlineshop.repository.CustomerRepository;
import com.vesna1010.onlineshop.repository.ProductRepository;
import com.vesna1010.onlineshop.service.ProductService;

@Transactional
@Service("productService")
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;
	private CustomerRepository customerRepository;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository, CustomerRepository customerRepository) {
		this.productRepository = productRepository;
		this.customerRepository = customerRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Product> findProductsByPage(Integer currentPage, Integer pageSize) {
		return (List<Product>) productRepository.findByPage(currentPage, pageSize);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Product> findProductsByCategoryAndPage(Category category, Integer currentPage, Integer pageSize) {
		return (List<Product>) productRepository.findByCategoryAndPage(category, currentPage, pageSize);
	}

	@Override
	@Transactional(readOnly = true)
	public Product findProductById(String id) {
		Optional<Product> optional = productRepository.findById(id);

		return optional.orElseThrow(() -> new ProductNotFoundException("Not Found Product By Id " + id));
	}

	@Override
	public Product saveProduct(Product product) {
		return (Product) productRepository.save(product);
	}

	@Override
	public void deleteProduct(Product product) {
		if (customerRepository.countByProduct(product) > 0) {
			throw new ProductCanNotDeleteException("Can Not Delete Product");
		}

		productRepository.delete(product);
	}

	@Override
	@Transactional(readOnly = true)
	public long countAllProducts() {
		return (long) productRepository.count();
	}

	@Override
	@Transactional(readOnly = true)
	public long countProductsByCategory(Category category) {
		return (long) productRepository.countByCategory(category);
	}

}
