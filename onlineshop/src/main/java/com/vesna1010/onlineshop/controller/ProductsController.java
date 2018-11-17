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
import com.vesna1010.onlineshop.model.Product;
import com.vesna1010.onlineshop.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductsController {

	@Autowired
	private ProductService service;

	@GetMapping
	public String renderPageWithProducts(
			@RequestParam(value = "page", defaultValue = "1") Integer currentPage,
			@RequestParam(value = "size", defaultValue = "10") Integer pageSize, 
			Model model) {

		model.addAttribute("products", service.findProductsByPage(currentPage, pageSize));
		model.addAttribute("count", service.countAllProducts());

		return "products/page";
	}

	@GetMapping(params = "categoryId")
	public String renderPageWithProductsByCategory(
			@RequestParam("categoryId") Category category,
			@RequestParam(value = "page", defaultValue = "1") Integer currentPage,
			@RequestParam(value = "size", defaultValue = "10") Integer pageSize, 
			Model model) {

		model.addAttribute("products", service.findProductsByCategoryAndPage(category, currentPage, pageSize));
		model.addAttribute("count", service.countProductsByCategory(category));

		return "products/page";
	}

	@GetMapping("/form")
	@ModelAttribute
	public Product product() {
		return new Product();
	}

	@PostMapping("/save")
	public String saveProductAndRenderForm(@ModelAttribute @Valid Product product, BindingResult result) {

		if (result.hasErrors()) {
			return "products/form";
		}

		service.saveProduct(product);

		return "redirect:/products/form";
	}

	@GetMapping("/edit")
	public String renderFormWithProduct(@RequestParam("productId") Product product, Model model) {
		model.addAttribute("product", product);

		return "products/form";
	}

	@GetMapping("/details")
	public String renderPageWithProductDetails(@RequestParam("productId") Product product, Model model) {
		model.addAttribute("product", product);

		return "products/details";
	}

	@GetMapping("/delete")
	public String deleteProductAndRenderRestProducts(@RequestParam("productId") Product product) {
		service.deleteProduct(product);

		return "redirect:/products";
	}

}
