package com.vesna1010.onlineshop.service;

import java.util.List;
import com.vesna1010.onlineshop.model.Customer;
import com.vesna1010.onlineshop.model.Product;

public interface CustomerService {

	List<Customer> findCustomersByPage(Integer currentPage, Integer pageSize);

	List<Customer> findCustomersByProductAndPage(Product product, Integer currentPage, Integer pageSize);

	void saveCustomer(Customer customer);

	long countAllCustomers();

	long countCustomersByProduct(Product product);

}
