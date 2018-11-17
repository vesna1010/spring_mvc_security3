package com.vesna1010.onlineshop.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.service.CategoryService;

@Controller
@RequestMapping("/categories")
public class CategoriesController {

	@Autowired
	private CategoryService service;

	@GetMapping
	public String renderPageWithCategories() {
		return "categories/page";
	}

	@GetMapping("/form")
	@ModelAttribute
	public Category category() {
		return new Category();
	}

	@PostMapping("/save")
	public String saveCategoryAndRenderForm(@ModelAttribute @Valid Category category, BindingResult result) {

		if (result.hasErrors()) {
			return "categories/form";
		}

		service.saveCategory(category);

		return "redirect:/categories/form";
	}

	@GetMapping("/edit")
	public String renderFormWithCategory(@RequestParam("categoryId") Category category, Model model) {
		model.addAttribute("category", category);

		return "categories/form";
	}

	@GetMapping("/delete")
	public String deleteCategoryAndRenderRestCategories(@RequestParam("categoryId") Category category) {
		service.deleteCategory(category);

		return "redirect:/categories";
	}

}
