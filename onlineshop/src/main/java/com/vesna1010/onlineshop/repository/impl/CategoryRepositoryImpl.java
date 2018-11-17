package com.vesna1010.onlineshop.repository.impl;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.repository.CategoryRepository;

@Repository("categoryRepository")
public class CategoryRepositoryImpl implements CategoryRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Category> findAll() {
		TypedQuery<Category> typedQuery = entityManager.createQuery("select c from Category c order by c.id asc",
				Category.class);

		return typedQuery.getResultList();
	}

	@Override
	public Optional<Category> findById(Long id) {
		TypedQuery<Category> typedQuery = entityManager.createQuery("select c from Category c where c.id=:id",
				Category.class);

		typedQuery.setParameter("id", id);

		try {
			return Optional.of(typedQuery.getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@Override
	public Category save(Category category) {
		if (category.getId() == null) {
			entityManager.persist(category);
			return category;
		} else {
			return entityManager.merge(category);
		}
	}

	@Override
	public void delete(Category category) {
		entityManager.remove(entityManager.contains(category) ? category : entityManager.merge(category));
	}

}
