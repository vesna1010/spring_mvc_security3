package com.vesna1010.onlineshop.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.vesna1010.onlineshop.model.User;
import com.vesna1010.onlineshop.service.UserService;
import com.vesna1010.onlineshop.validator.UserValidator;

@Controller
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UserService service;
	@Autowired
	private UserValidator validator;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	@GetMapping
	public String renderPageWithUsers(
			@RequestParam(value = "page", defaultValue = "1") Integer currentPage,
			@RequestParam(value = "size", defaultValue = "10") Integer pageSize,
			Model model) {

		model.addAttribute("users", service.findUsersByPage(currentPage, pageSize));
		model.addAttribute("count", service.countAllUsers());

		return "users/page";
	}

	@GetMapping("/form")
	@ModelAttribute
	public User user() {
		return new User();
	}

	@PostMapping("/save")
	public String saveUserAndRenderForm(@ModelAttribute @Validated User user, BindingResult result) {

		if (!result.hasErrors()) {
			service.saveUser(user);
		}

		return "users/form";
	}

	@GetMapping(value = "/edit")
	public String renderFormWithUser(Principal principal, Model model) {
		String username = principal.getName();

		model.addAttribute("user", service.findUserByUsername(username));

		return "users/form";
	}

	@GetMapping("/delete")
	public String deleteUserAndRenderRestUsers(@RequestParam("username") User user) {
		service.deleteUser(user);

		return "redirect:/users";
	}

	@GetMapping("/disable")
	public String disableUserAndRenderRestUsers(@RequestParam("username") User user) {
		service.disableUser(user);

		return "redirect:/users";
	}

}
