package com.ks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ks.entity.Admin;
import com.ks.entity.Category;
import com.ks.entity.Product;
import com.ks.service.AdminService;
import com.ks.service.CategoryService;
import com.ks.service.ProductService;

import jakarta.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin") // Base URL for admin-related operations
public class AdminController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private AdminService adminService;

	// ====================================================================================
	                                // Admin Dashboard
	// ====================================================================================
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/")
	public String adminIndex() {
		return "admin/admin-index";
	}

	// ====================================================================================
	                               // Category Management
	// ====================================================================================

	// Display all categories

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/category")
	public String categoryPage(Model model) {
		List<Category> categories = categoryService.getAllCategory();
		model.addAttribute("categories", categories);
		return "admin/category";
	}
//---------------------------------------------------------------------------------------------------------------------------

	// Save a new category

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/category/save")
	public String saveCategory(@RequestParam("name") String name, @RequestParam("image") MultipartFile image,
			RedirectAttributes redirectAttributes) {
		try {
			// Validate file size and type
			long maxFileSize = 25 * 1024 * 1024; // 25MB limit
			if (image.getSize() > maxFileSize) {
				redirectAttributes.addFlashAttribute("errorMessage",
						"File size exceeds 25MB. Please upload a smaller image.");
				return "redirect:/admin/category";
			}

			String contentType = image.getContentType();
			if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
				redirectAttributes.addFlashAttribute("errorMessage",
						"Invalid file type. Only JPEG and PNG images are allowed.");
				return "redirect:/admin/category";
			}

			// Save category with image
			categoryService.saveCategory(name, image);
			redirectAttributes.addFlashAttribute("successMessage", "Category added successfully!");
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Error uploading image: " + e.getMessage());
		}
		return "redirect:/admin/category";
	}
//---------------------------------------------------------------------------------------------------------------------------

	// Edit a category

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/category/edit/{id}")
	public String editCategory(@PathVariable Long id, Model model) {
		Optional<Category> categoryOpt = categoryService.findById(id);
		if (!categoryOpt.isPresent()) {
			model.addAttribute("errorMessage", "Category not found");
			return "redirect:/admin/category";
		}
		model.addAttribute("category", categoryOpt.get());
		return "admin/edit-category";
	}
//---------------------------------------------------------------------------------------------------------------------------

	// Update a category
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/category/update")
	public String updateCategory(@RequestParam("id") Long id, @RequestParam("name") String name,
			@RequestParam("image") MultipartFile image, RedirectAttributes redirectAttributes) {
		try {
			Optional<Category> categoryOpt = categoryService.findById(id);
			if (!categoryOpt.isPresent()) {
				redirectAttributes.addFlashAttribute("errorMessage", "Category not found!");
				return "redirect:/admin/category";
			}
			Category category = categoryOpt.get();
			category.setName(name);

			// Update image if provided
			if (!image.isEmpty()) {
				String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
				Path filePath = Paths.get("uploads/category_images/" + fileName);
				Files.createDirectories(filePath.getParent());
				Files.write(filePath, image.getBytes());
				category.setImagePath("/uploads/" + fileName);
			}

			categoryService.updateCategory(category);
			redirectAttributes.addFlashAttribute("successMessage", "Category updated successfully!");
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Error uploading image: " + e.getMessage());
		}
		return "redirect:/admin/category";
	}
//---------------------------------------------------------------------------------------------------------------------------

	// Delete a category

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/category/delete/{id}")
	public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			Category category = categoryService.getCategoryById(id);
			if (category != null && category.getImagePath() != null) {
				Path filePath = Paths
						.get("./uploads/category_images/" + Paths.get(category.getImagePath()).getFileName());
				Files.deleteIfExists(filePath);
			}

			categoryService.deleteCategory(id);
			redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully!");
		} catch (DataIntegrityViolationException e) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Cannot delete category! Products exist under this category.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Error deleting category: " + e.getMessage());
		}
		return "redirect:/admin/category";
	}
//---------------------------------------------------------------------------------------------------------------------------

	// ====================================================================================
	                                   // Product Management
	// ====================================================================================

//---------------------------------------------------------------------------------------------------------------------------

	// Display add product page

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/addproduct")
	public String addProduct(Model model) {
		List<Category> categories = categoryService.getAllCategory();
		model.addAttribute("categories", categories);
		return "admin/add-product";
	}

	// Save a new product
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/product/save")
	public String saveProduct(@RequestParam("title") String title, @RequestParam("description") String description,
			@RequestParam("categoryId") Long categoryId, @RequestParam("stock") int stock,
			@RequestParam("price") Double price, @RequestParam("image") MultipartFile image,
			RedirectAttributes redirectAttributes) {
		try {
			// Validate file size and type
			long maxFileSize = 25 * 1024 * 1024; // 25MB limit
			if (image.getSize() > maxFileSize) {
				redirectAttributes.addFlashAttribute("errorMessage",
						"File size exceeds 25MB. Please upload a smaller image.");
				return "redirect:/admin/addproduct";
			}

			String contentType = image.getContentType();
			if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
				redirectAttributes.addFlashAttribute("errorMessage",
						"Invalid file type. Only JPEG and PNG images are allowed.");
				return "redirect:/admin/addproduct";
			}

			// Fetch category and save product
			Category category = categoryService.findById(categoryId)
					.orElseThrow(() -> new IllegalArgumentException("Category not found"));
			productService.saveProduct(title, description, category, stock, price, image);
			redirectAttributes.addFlashAttribute("successMessage", "Product added successfully!");
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Error uploading image: " + e.getMessage());
		}
		return "redirect:/admin/addproduct";
	}
//---------------------------------------------------------------------------------------------------------------------------

	// Display all products

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/viewproducts")
	public String viewProducts(Model model) {
		List<Product> products = productService.getAllProducts();
		List<Category> categories = categoryService.getAllCategory();
		model.addAttribute("products", products);
		model.addAttribute("categories", categories);
		return "admin/view-products";
	}
//---------------------------------------------------------------------------------------------------------------------------

	// Edit a product

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/editproduct/{id}")
	public String editProduct(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
		Product product = productService.getProductById(id);
		if (product != null) {
			List<Category> categories = categoryService.getAllCategory();
			model.addAttribute("product", product);
			model.addAttribute("categories", categories);
			return "admin/edit-product";
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
			return "redirect:/admin/viewproducts";
		}
	}
//---------------------------------------------------------------------------------------------------------------------------

	// Update a product

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/updateproduct/{id}")
	public String updateProduct(@PathVariable Long id, @RequestParam("title") String title,
			@RequestParam("description") String description, @RequestParam("categoryId") Long categoryId,
			@RequestParam("stock") int stock, @RequestParam("price") Double price,
			@RequestParam("image") MultipartFile image, RedirectAttributes redirectAttributes) {
		try {
			Product product = productService.getProductById(id);
			if (product != null) {
				Category category = categoryService.findById(categoryId)
						.orElseThrow(() -> new IllegalArgumentException("Category not found"));

				product.setTitle(title);
				product.setDescription(description);
				product.setCategory(category);
				product.setStock(stock);
				product.setPrice(price);

				// Update image if provided
				if (!image.isEmpty()) {
					long maxFileSize = 25 * 1024 * 1024; // 25MB limit
					if (image.getSize() > maxFileSize) {
						redirectAttributes.addFlashAttribute("errorMessage",
								"File size exceeds 25MB. Please upload a smaller image.");
						return "redirect:/admin/editproduct/" + id;
					}

					String contentType = image.getContentType();
					if (!(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
						redirectAttributes.addFlashAttribute("errorMessage",
								"Invalid file type. Only JPEG and PNG images are allowed.");
						return "redirect:/admin/editproduct/" + id;
					}

					productService.updateProductImage(product, image);
				}

				productService.saveUpdatedProduct(product);
				redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
			} else {
				redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
			}
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Error updating image: " + e.getMessage());
		}
		return "redirect:/admin/viewproducts";
	}
//---------------------------------------------------------------------------------------------------------------------------

	// Delete a product

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/deleteproduct/{id}")
	public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			Product product = productService.getProductById(id);
			if (product != null && product.getImagePath() != null) {
				Path filePath = Paths
						.get("./uploads/product_images/" + Paths.get(product.getImagePath()).getFileName());
				Files.deleteIfExists(filePath);
			}

			productService.deleteProductById(id);
			redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product: " + e.getMessage());
		}
		return "redirect:/admin/viewproducts";
	}
//---------------------------------------------------------------------------------------------------------------------------

	// ====================================================================================
	                                    // Admin Management
	// ====================================================================================

//---------------------------------------------------------------------------------------------------------------------------

	// Display add admin page

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/addadmin")
	public String addAdmin(Model model) {
		List<Admin> adminList = adminService.getAllAdmins();
		model.addAttribute("admins", adminList);
		model.addAttribute("admin", new Admin());
		return "admin/addadmin";
	}
//---------------------------------------------------------------------------------------------------------------------------

	// Save a new admin

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addadmin-handle")
	public String addAdminHandle(@Valid @ModelAttribute("admin") Admin admin, BindingResult bindingResult,
			@RequestParam(value = "profileImage", required = false) MultipartFile image,
			RedirectAttributes redirectAttributes, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("admins", adminService.getAllAdmins());
			return "admin/addadmin";
		}

		if (admin.getPassword() == null || admin.getPassword().length() < 8) {
			redirectAttributes.addFlashAttribute("errorMessage", "Password must be at least 8 characters long");
			return "redirect:/admin/addadmin";
		}

		try {
			admin.setRole("ROLE_ADMIN");
			adminService.saveAdmin(admin);
			redirectAttributes.addFlashAttribute("successMessage", "Admin added successfully!");
			return "redirect:/admin/addadmin";
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			model.addAttribute("admins", adminService.getAllAdmins());
			return "admin/addadmin";
		} catch (Exception e) {
			model.addAttribute("errorMessage", "An error occurred. Please try again.");
			model.addAttribute("admins", adminService.getAllAdmins());
			return "admin/addadmin";
		}
	}
//---------------------------------------------------------------------------------------------------------------------------

	// Delete an admin

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/delete/{id}")
	public String deleteAdmin(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			adminService.deleteAdmin(id);
			redirectAttributes.addFlashAttribute("successMessage", "Admin deleted successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete admin. Please try again.");
		}
		return "redirect:/admin/addadmin";
	}
//---------------------------------------------------------------------------------------------------------------------------

	// Display update admin form

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/update/{id}")
	public String showUpdateForm(@PathVariable Long id, Model model) {
		Admin admin = adminService.findById(id);
		model.addAttribute("admin", admin);
		return "admin/updateadmin";
	}
//---------------------------------------------------------------------------------------------------------------------------

	// Update an admin

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/update/{id}")
	public String updateAdmin(@PathVariable Long id, @ModelAttribute @Valid Admin admin, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "admin/updateadmin";
		}

		try {
			adminService.updateAdmin(id, admin);
			redirectAttributes.addFlashAttribute("successMessage", "Admin updated successfully!");
			return "redirect:/admin/addadmin";
		} catch (DataIntegrityViolationException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Email already exists. Please use a different email.");
			return "redirect:/admin/update/" + id;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "An error occurred. Please try again.");
			return "redirect:/admin/update/" + id;
		}
	}
//---------------------------------------------------------------------------------------------------------------------------

}