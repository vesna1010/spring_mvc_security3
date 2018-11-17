package com.vesna1010.onlineshop.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vesna1010.onlineshop.exception.ProductCanNotOrderException;
import com.vesna1010.onlineshop.model.Customer;
import com.vesna1010.onlineshop.model.Product;
import com.vesna1010.onlineshop.repository.CustomerRepository;
import com.vesna1010.onlineshop.repository.ProductRepository;
import com.vesna1010.onlineshop.service.CustomerService;

@Transactional
@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

	private CustomerRepository customerRepository;
	private ProductRepository productRepository;

	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepository, ProductRepository productRepository) {
		this.customerRepository = customerRepository;
		this.productRepository = productRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Customer> findCustomersByPage(Integer currentPage, Integer pageSize) {
		return (List<Customer>) customerRepository.findByPage(currentPage, pageSize);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Customer> findCustomersByProductAndPage(Product product, Integer currentPage, Integer pageSize) {
		return (List<Customer>) customerRepository.findByProductAndPage(product, currentPage, pageSize);
	}

	@Override
	public void saveCustomer(Customer customer) {
		Map<Product, Integer> products = customer.getProducts();

		products.forEach((product, numberOfProducts) -> {
			if(product.getStocks() < numberOfProducts) {
				throw new ProductCanNotOrderException("We do not have enough stock of the product:  " + product.getName());
			}
			
			product.setStocks(product.getStocks() - numberOfProducts);
			productRepository.save(product);
		});

		customerRepository.save(customer);
	}

	@Override
	@Transactional(readOnly = true)
	public long countAllCustomers() {
		return (long) customerRepository.count();
	}

	@Override
	@Transactional(readOnly = true)
	public long countCustomersByProduct(Product product) {
		return (long) customerRepository.countByProduct(product);
	}

}
