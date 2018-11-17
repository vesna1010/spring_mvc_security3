package com.vesna1010.onlineshop.repository.impl;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.model.Product;
import com.vesna1010.onlineshop.repository.ProductRepository;

@Repository("productRepository")
public class ProductRepositoryImpl implements ProductRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Product> findByPage(Integer currentPage, Integer pageSize) {
		TypedQuery<Product> typedQuery = entityManager.createQuery("select p from Product p order by p.id asc",
				Product.class);

		typedQuery.setFirstResult((currentPage - 1) * pageSize);
		typedQuery.setMaxResults(pageSize);

		return typedQuery.getResultList();
	}

	@Override
	public List<Product> findByCategoryAndPage(Category category, Integer currentPage, Integer pageSize) {
		TypedQuery<Product> typedQuery = entityManager
				.createQuery("select p from Product p where p.category=:category order by p.id asc", Product.class);

		typedQuery.setParameter("category", category);
		typedQuery.setFirstResult((currentPage - 1) * pageSize);
		typedQuery.setMaxResults(pageSize);

		return typedQuery.getResultList();
	}

	@Override
	public Optional<Product> findById(String id) {
		TypedQuery<Product> typedQuery = entityManager.createQuery("select p from Product p where p.id=:id",
				Product.class);

		typedQuery.setParameter("id", id);

		try {
			return Optional.of(typedQuery.getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@Override
	public Product save(Product product) {
		if (existsById(product.getId())) {
			return entityManager.merge(product);
		} else {
			entityManager.persist(product);
			return product;
		}
	}

	private boolean existsById(String id) {
		TypedQuery<Long> typedQuery = entityManager.createQuery("select count(p) from Product p where p.id=:id",
				Long.class);

		typedQuery.setParameter("id", id);

		return (typedQuery.getSingleResult() > 0);
	}

	@Override
	public void delete(Product product) {
		entityManager.remove(entityManager.contains(product) ? product : entityManager.merge(product));
	}

	@Override
	public long count() {
		TypedQuery<Long> typedQuery = entityManager.createQuery("select count(p) from Product p", Long.class);

		return typedQuery.getSingleResult();
	}

	@Override
	public long countByCategory(Category category) {
		TypedQuery<Long> typedQuery = entityManager
				.createQuery("select count(p) from Product p where p.category=:category", Long.class);

		typedQuery.setParameter("category", category);

		return typedQuery.getSingleResult();
	}

}
