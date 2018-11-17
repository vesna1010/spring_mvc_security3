package com.vesna1010.onlineshop.repository.impl;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import com.vesna1010.onlineshop.model.User;
import com.vesna1010.onlineshop.repository.UserRepository;

@Repository("userRepository")
public class UserRepositoryImpl implements UserRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<User> findByPage(Integer currentPage, Integer pageSize) {
		TypedQuery<User> typedQuery = entityManager.createQuery("select u from User u order by u.username asc",
				User.class);

		typedQuery.setFirstResult((currentPage - 1) * pageSize);
		typedQuery.setMaxResults(pageSize);

		return typedQuery.getResultList();
	}

	@Override
	public Optional<User> findByUsername(String username) {
		TypedQuery<User> typedQuery = entityManager.createQuery("select u from User u where u.username=:username",
				User.class);

		typedQuery.setParameter("username", username);

		try {
			return Optional.of(typedQuery.getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@Override
	public User save(User user) {
		if (existsByUsername(user.getUsername())) {
			return entityManager.merge(user);
		} else {
			entityManager.persist(user);
			return user;
		}
	}

	private boolean existsByUsername(String username) {
		TypedQuery<Long> typedQuery = entityManager
				.createQuery("select count(u) from User u where u.username=:username", Long.class);

		typedQuery.setParameter("username", username);

		return (typedQuery.getSingleResult() > 0);
	}

	@Override
	public void disableUser(User user) {
		user.setEnabled(false);
		entityManager.merge(user);
	}

	@Override
	public void deleteUser(User user) {
		entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
	}

	@Override
	public long count() {
		TypedQuery<Long> typedQuery = entityManager.createQuery("select count(u) from User u", Long.class);

		return typedQuery.getSingleResult();
	}

}
