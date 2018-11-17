package com.vesna1010.onlineshop.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.vesna1010.onlineshop.exception.CategoryCanNotDeleteException;
import com.vesna1010.onlineshop.exception.CategoryNotFoundException;
import com.vesna1010.onlineshop.exception.ProductCanNotDeleteException;
import com.vesna1010.onlineshop.exception.ProductNotFoundException;
import com.vesna1010.onlineshop.exception.UserNotFoundException;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.service.CategoryService;

@ControllerAdvice
public class GlobalController {

	@Autowired
	private CategoryService service;

	@ModelAttribute("categories")
	public List<Category> categories() {
		return service.findAllCategories();
	}

	@ExceptionHandler
	public String handleCategoryNotFoundException(CategoryNotFoundException ex, Model model) {
		model.addAttribute("message", ex.getMessage());

		return "exceptions/page";
	}

	@ExceptionHandler
	public String handleCategoryCanNotDeleteException(CategoryCanNotDeleteException ex, Model model) {
		model.addAttribute("message", ex.getMessage());

		return "exceptions/page";
	}

	@ExceptionHandler
	public String handleProductNotFoundException(ProductNotFoundException ex, Model model) {
		model.addAttribute("message", ex.getMessage());

		return "exceptions/page";
	}
	
	@ExceptionHandler
	public String handleProductCanNotDeleteException(ProductCanNotDeleteException ex, Model model) {
		model.addAttribute("message", ex.getMessage());

		return "exceptions/page";
	}

	@ExceptionHandler
	public String handleUserNotFoundException(UserNotFoundException ex, Model model) {
		model.addAttribute("message", ex.getMessage());

		return "exceptions/page";
	}
	
	@ExceptionHandler
	public String handleUsernameNotFoundException(UsernameNotFoundException ex, Model model) {
		model.addAttribute("message", ex.getMessage());

		return "exceptions/page";
	}

	@ExceptionHandler
	public String handleConversionFailedException(ConversionFailedException ex, Model model) {
		model.addAttribute("message", ex.getCause().getMessage());

		return "exceptions/page";
	}

}