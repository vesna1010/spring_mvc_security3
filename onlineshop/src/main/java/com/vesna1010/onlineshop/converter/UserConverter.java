package com.vesna1010.onlineshop.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import com.vesna1010.onlineshop.model.User;
import com.vesna1010.onlineshop.service.UserService;

public class UserConverter implements Converter<String, User> {

	@Autowired
	private UserService service;

	@Override
	public User convert(String username) {
		return ((username == null || username.isEmpty()) ? null : service.findUserByUsername(username));
	}

}
