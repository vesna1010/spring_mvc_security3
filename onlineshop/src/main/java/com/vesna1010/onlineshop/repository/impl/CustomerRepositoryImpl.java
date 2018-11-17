package com.vesna1010.onlineshop.repository.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import com.vesna1010.onlineshop.model.Customer;
import com.vesna1010.onlineshop.model.Product;
import com.vesna1010.onlineshop.repository.CustomerRepository;

@Repository("customerRepository")
public class CustomerRepositoryImpl implements CustomerRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Customer> findByPage(Integer currentPage, Integer pageSize) {
		TypedQuery<Customer> typedQuery = entityManager
				.createQuery("select c from Customer c order by c.date desc, c.id asc", Customer.class);

		typedQuery.setFirstResult((currentPage - 1) * pageSize);
		typedQuery.setMaxResults(pageSize);

		return typedQuery.getResultList();
	}

	@Override
	public List<Customer> findByProductAndPage(Product product, Integer currentPage, Integer pageSize) {
		TypedQuery<Customer> typedQuery = entityManager.createQuery(
				"select c from Customer c join c.products p where key(p)=:product order by c.date desc, c.id asc",
				Customer.class);

		typedQuery.setParameter("product", product);
		typedQuery.setFirstResult((currentPage - 1) * pageSize);
		typedQuery.setMaxResults(pageSize);

		return typedQuery.getResultList();
	}

	@Override
	public void save(Customer customer) {
		entityManager.persist(customer);
	}

	@Override
	public long count() {
		TypedQuery<Long> typedQuery = entityManager.createQuery("select count(c) from Customer c", Long.class);

		return typedQuery.getSingleResult();
	}

	@Override
	public long countByProduct(Product product) {
		TypedQuery<Long> typedQuery = entityManager
				.createQuery("select count(c) from Customer c join c.products p where key(p)=:product", Long.class);

		typedQuery.setParameter("product", product);

		return typedQuery.getSingleResult();
	}

}
