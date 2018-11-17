package com.vesna1010.onlineshop.repository;

import java.util.List;
import java.util.Optional;
import com.vesna1010.onlineshop.model.User;

public interface UserRepository {

	List<User> findByPage(Integer currentPage, Integer pageSize);
	
	Optional<User> findByUsername(String username);
	
	User save(User user);

	void disableUser(User user);

	void deleteUser(User user);

	long count();

}
