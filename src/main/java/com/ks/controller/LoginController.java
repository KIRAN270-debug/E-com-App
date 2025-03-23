package com.ks.controller;

import com.ks.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	@Autowired
	private AuthService authService; // Service for handling authentication logic

//---------------------------------------------------------------------------------------------------------------------------

	// Display the login form

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		return "login"; // Returns the login view (login.html or similar)
	}

//---------------------------------------------------------------------------------------------------------------------------

	// Handle login form submission

	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, Model model) {
		// Authenticate the user using the AuthService
		String role = authService.authenticate(username, password);

		// Redirect based on the user's role
		if ("ROLE_ADMIN".equals(role)) {
			return "redirect:/admin/dashboard"; // Redirect to admin dashboard
		} else if ("ROLE_USER".equals(role)) {
			return "redirect:/user/dashboard"; // Redirect to user dashboard
		} else {
			// If authentication fails, show an error message
			model.addAttribute("errorMessage", "Invalid email or password");
			return "login"; // Return to the login page with an error message
		}
	}
//---------------------------------------------------------------------------------------------------------------------------
	// Display the forgot-password page

	@GetMapping("/forgot-password")
	public String forgotPasswordForm() {
		return "forgot-password";
	}

//---------------------------------------------------------------------------------------------------------------------------
//	// Handle forgot-password form submission
//	@PostMapping("/forgotpassword")
//	public String forgotPassword() {
//		return "login";
//	}
//---------------------------------------------------------------------------------------------------------------------------

}