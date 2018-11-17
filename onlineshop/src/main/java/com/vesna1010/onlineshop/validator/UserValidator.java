package com.vesna1010.onlineshop.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.vesna1010.onlineshop.enums.Authority;
import com.vesna1010.onlineshop.model.User;

public class UserValidator implements Validator {

	private String usernamePattern = "^\\w{8,15}$";
	private String passwordPattern = "^\\S{8,15}$";
	private String emailPattern = "^[a-zA-Z0-9_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

	@Override
	public boolean supports(Class<?> cls) {
		return cls.equals(User.class);
	}

	@Override
	public void validate(Object object, Errors errors) {
		User user = (User) object;

		if (isInvalidUsername(user.getUsername())) {
			errors.rejectValue("username", "user.username");
		}

		if (isInvalidPassword(user.getPassword())) {
			errors.rejectValue("password", "user.password");
		}

		if (isInvalidEmail(user.getEmail())) {
			errors.rejectValue("email", "user.email");
		}

		if (!isInvalidPassword(user.getPassword())
				&& isNotEqualsPasswords(user.getPassword(), user.getConfirmPassword())) {
			errors.rejectValue("confirmPassword", "user.passwordConfirmDifferent");
		}

		if (isInvalidAuthority(user.getAuthority())) {
			errors.rejectValue("authority", "user.authority");
		}
	}

	private boolean isInvalidUsername(String username) {
		return (!username.matches(usernamePattern));
	}

	private boolean isInvalidPassword(String password) {
		return (!password.matches(passwordPattern));
	}

	private boolean isInvalidEmail(String email) {
		return (!email.matches(emailPattern));
	}

	private boolean isNotEqualsPasswords(String password, String confirmPassword) {
		return (!password.equals(confirmPassword));
	}

	private boolean isInvalidAuthority(Authority authority) {
		return (authority == null);
	}

}
