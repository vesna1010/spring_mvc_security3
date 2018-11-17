package com.vesna1010.onlineshop.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vesna1010.onlineshop.exception.UserNotFoundException;
import com.vesna1010.onlineshop.model.User;
import com.vesna1010.onlineshop.repository.UserRepository;
import com.vesna1010.onlineshop.service.UserService;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {

	private UserRepository repository;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> findUsersByPage(Integer currentPage, Integer pageSize) {
		return (List<User>) repository.findByPage(currentPage, pageSize);
	}

	@Override
	@Transactional(readOnly = true)
	public User findUserByUsername(String username) {
		Optional<User> optional = repository.findByUsername(username);

		return optional.orElseThrow(() -> new UserNotFoundException("Not Found User By Username " + username));
	}

	@Override
	public User saveUser(User user) {
		setUserPassword(user);

		return (User) repository.save(user);
	}

	private void setUserPassword(User user) {
		String password = user.getPassword();

		password = passwordEncoder.encode(password);
		user.setPassword(password);
	}

	@Override
	public void disableUser(User user) {
		repository.disableUser(user);
	}

	@Override
	public void deleteUser(User user) {
		repository.deleteUser(user);
	}

	@Override
	@Transactional(readOnly = true)
	public long countAllUsers() {
		return (long) repository.count();
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return (User) findUserByUsername(username);
	}

}
