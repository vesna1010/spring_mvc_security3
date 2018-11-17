package com.vesna1010.onlineshop.repository;

import java.util.List;
import com.vesna1010.onlineshop.model.Customer;
import com.vesna1010.onlineshop.model.Product;

public interface CustomerRepository {

	List<Customer> findByPage(Integer currentPage, Integer pageSize);

	List<Customer> findByProductAndPage(Product product, Integer currentPage, Integer pageSize);

	void save(Customer customer);

	long count();

	long countByProduct(Product product);

}
