package com.vesna1010.onlineshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.service.ProductService;

@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	private ProductService service;

	@GetMapping
	public String renderHomePageWithProducts(
			@RequestParam(value = "page", defaultValue = "1") Integer currentPage,
			@RequestParam(value = "size", defaultValue = "10") Integer pageSize, 
			Model model) {

		model.addAttribute("products", service.findProductsByPage(currentPage, pageSize));
		model.addAttribute("count", service.countAllProducts());

		return "home";
	}

	@GetMapping(params = "categoryId")
	public String renderHomePageWithProductsByCategory(
			@RequestParam("categoryId") Category category,
			@RequestParam(value = "page", defaultValue = "1") Integer currentPage,
			@RequestParam(value = "size", defaultValue = "10") Integer pageSize, 
			Model model) {

		model.addAttribute("products", service.findProductsByCategoryAndPage(category, currentPage, pageSize));
		model.addAttribute("count", service.countProductsByCategory(category));

		return "home";
	}

}
