package com.vesna1010.onlineshop.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.vesna1010.onlineshop.exception.ProductCanNotOrderException;
import com.vesna1010.onlineshop.model.Customer;
import com.vesna1010.onlineshop.model.Product;
import com.vesna1010.onlineshop.service.CustomerService;
import com.vesna1010.onlineshop.service.ProductService;

@Controller
@RequestMapping("/customers")
public class CustomersController {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private ProductService productService;

	@GetMapping
	public String renderPageWithCustomers(
			@RequestParam(value = "page", defaultValue = "1") Integer currentPage,
			@RequestParam(value = "size", defaultValue = "10") Integer pageSize, 
			Model model) {

		model.addAttribute("customers", customerService.findCustomersByPage(currentPage, pageSize));
		model.addAttribute("count", customerService.countAllCustomers());

		return "customers/page";
	}

	@GetMapping(params = "productId")
	public String renderPageWithCustomersByProduct(
			@RequestParam("productId") Product product,
			@RequestParam(value = "page", defaultValue = "1") Integer currentPage,
			@RequestParam(value = "size", defaultValue = "10") Integer pageSize, 
			Model model) {

		model.addAttribute("customers", customerService.findCustomersByProductAndPage(product, currentPage, pageSize));
		model.addAttribute("count", customerService.countCustomersByProduct(product));

		return "customers/page";
	}

	@GetMapping("/addProduct")
	public String addProduct(@RequestParam String productId, 
			@RequestParam(defaultValue = "1") Integer numberOfProducts,
			HttpServletResponse response) {
		
		Cookie cookie = new Cookie(("PRODUCTID" + productId),
				String.valueOf(numberOfProducts > 1 ? numberOfProducts : 1));

		cookie.setMaxAge(30 * 24 * 60 * 60);
		response.addCookie(cookie);

		return "redirect:/customers/products";
	}

	@GetMapping("/removeProduct")
	public String removeProduct(@RequestParam String productId, HttpServletResponse response) {
		Cookie cookie = new Cookie(("PRODUCTID" + productId), "0");

		cookie.setMaxAge(0);
		response.addCookie(cookie);

		return "redirect:/customers/products";
	}

	@GetMapping("/products")
	public String renderPageWithSelectedProducts(HttpServletRequest request, Model model) {
		model.addAttribute("customer", new Customer(getSelectedProducts(request)));

		return "customers/products";
	}

	@GetMapping("/form")
	@ModelAttribute
	public Customer customer(HttpServletRequest request) {
		return new Customer(getSelectedProducts(request));
	}

	public Map<Product, Integer> getSelectedProducts(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		Map<Product, Integer> products = new HashMap<>();

		Arrays.stream(cookies).forEach(cookie -> {
			String key = cookie.getName();

			if (key.matches("^PRODUCTID[\\d]{13}$")) {
				String productId = key.substring(9);
				Product product = productService.findProductById(productId);
				Integer numberOfProducts = Integer.valueOf(cookie.getValue());

				products.put(product, numberOfProducts);
			}
		});

		return products;
	}

	@PostMapping("/save")
	public String saveCustomerAndRenderHomePage(@ModelAttribute @Valid Customer customer, 
			BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			return "customers/form";
		}

		try {
			customerService.saveCustomer(customer);
			model.addAttribute("message", "These products are ordered!");
		} catch (ProductCanNotOrderException e) {
			model.addAttribute("message", e.getMessage());
		}

		return "customers/products";
	}

}
