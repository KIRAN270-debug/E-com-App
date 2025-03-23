package com.ks.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ks.entity.Category;
import com.ks.entity.Product;
import com.ks.entity.User;
import com.ks.service.CategoryService;
import com.ks.service.ProductService;
import com.ks.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;
//---------------------------------------------------------------------------------------------------------------------------
	@GetMapping("/")
	public String index(Model model) {
		// Fetch all categories and display them on the home page
		List<Category> categories = categoryService.getAllCategory();
		model.addAttribute("categories", categories);
		return "index";
	}
//---------------------------------------------------------------------------------------------------------------------------

	// ====================================================================================
	                                 // User Registration
	// ====================================================================================
	
//---------------------------------------------------------------------------------------------------------------------------

	// Display the registration form
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}
//---------------------------------------------------------------------------------------------------------------------------

	// Handle user registration
	
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
			@RequestParam(value = "profileImage", required = false) MultipartFile image,
			RedirectAttributes redirectAttributes, Model model) {

		// Check for validation errors
		if (bindingResult.hasErrors()) {
			System.out.println("Validation errors: " + bindingResult.getAllErrors());
			return "register"; // Return to the registration page if there are errors
		}

		try {
			// Set user role and save the user
			user.setRole("ROLE_USER");
			userService.saveUser(user, image);

			// Show success message and redirect to the login page
			redirectAttributes.addFlashAttribute("successMessage", "Registered successfully! Please login.");
			return "redirect:/login";
		} catch (IllegalArgumentException e) {
			// Handle password mismatch error
			System.out.println("Error: " + e.getMessage());
			bindingResult.rejectValue("confirmPassword", "error.user", e.getMessage());
			return "register";
		} catch (IllegalStateException e) {
			// Handle email already exists error
			System.out.println("Error: " + e.getMessage());
			model.addAttribute("errorMessage", e.getMessage());
			return "register";
		} catch (IOException e) {
			// Handle image upload error
			System.out.println("Error: " + e.getMessage());
			model.addAttribute("errorMessage", "Error uploading image: " + e.getMessage());
			return "register";
		}
	}
//---------------------------------------------------------------------------------------------------------------------------

	// ====================================================================================
	// Product Display
	// ====================================================================================
	
//---------------------------------------------------------------------------------------------------------------------------
	// Display products under a specific category
	
	@GetMapping("/viewproducts/{id}")
	public String viewProductsCustomer(@PathVariable("id") Long categoryId, Model model) {
		// Fetch category details
		Category category = categoryService.findById(categoryId)
				.orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));

		// Fetch products belonging to the category
		List<Product> products = productService.findByCategoryId(categoryId);

		// Add data to the model
		model.addAttribute("products", products);
		model.addAttribute("category", category);

		return "view-product"; // Return the view-product page
	}
//---------------------------------------------------------------------------------------------------------------------------
}