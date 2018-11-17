package com.vesna1010.onlineshop.service;

import java.util.List;

import com.vesna1010.onlineshop.model.User;

public interface UserService {

	List<User> findUsersByPage(Integer currentPage, Integer pageSize);

	User findUserByUsername(String username);

	User saveUser(User user);

	void disableUser(User user);

	void deleteUser(User user);

	long countAllUsers();

}
