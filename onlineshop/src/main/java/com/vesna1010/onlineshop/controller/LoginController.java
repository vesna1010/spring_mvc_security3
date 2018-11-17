package com.vesna1010.onlineshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String renderLoginForm() {
		return "login/form";
	}

	@GetMapping("/denied")
	public String renderDeniedPage(Model model) {
		model.addAttribute("message", "Access Denied");

		return "exceptions/page";
	}

}
