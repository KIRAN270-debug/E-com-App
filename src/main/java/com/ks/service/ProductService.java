package com.ks.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ks.entity.Category;
import com.ks.entity.Product;
import com.ks.repository.ProductRepository;

@Service // Marks this class as a Spring service component
public class ProductService {

	@Autowired
	private ProductRepository productRepository; // Repository for accessing product data

// ---------------------------------------------------------------------------------------------------------------------------

	// Directory where product images will be stored
	
	private static final String UPLOAD_DIR = "uploads/product_images/";

// ---------------------------------------------------------------------------------------------------------------------------

	// Save a new product with an image
	
	public Product saveProduct(String title, String description, Category category, int stock, Double price,
			MultipartFile image) throws IOException {
		if (image.isEmpty()) {
			throw new IllegalArgumentException("Image file is required!"); // Throw exception if no image is provided
		}

		// Generate a unique file name using the current timestamp and original file
		// name
		String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
		Path filePath = Paths.get(UPLOAD_DIR + fileName);

		// Ensure the directory exists before saving the file
		Files.createDirectories(filePath.getParent());

		// Save the image file to the specified directory
		Files.write(filePath, image.getBytes());

		// Create and save the product with the image path
		Product product = new Product(title, description, category, price, stock,
				"/uploads/product_images/" + fileName);
		return productRepository.save(product); // Save the product to the database
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Get all products
	
	public List<Product> getAllProducts() {
		return productRepository.findAll(); // Returns a list of all products
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Get a product by ID
	
	public Product getProductById(Long id) {
		return productRepository.findById(id).orElse(null); // Returns the product or null if not found
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Update product details
	
	public Product saveUpdatedProduct(Product product) {
		return productRepository.save(product); // Saves the updated product to the database
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Update product image
	
	public void updateProductImage(Product product, MultipartFile image) throws IOException {
		if (image.isEmpty()) {
			return; // If no new image is provided, keep the existing image
		}

		// Generate a unique file name using the current timestamp and original file
		// name
		String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
		Path filePath = Paths.get(UPLOAD_DIR + fileName);

		// Ensure the directory exists before saving the file
		Files.createDirectories(filePath.getParent());

		// Save the new image file to the specified directory
		Files.write(filePath, image.getBytes());

		// Update the product's image path and save it to the database
		product.setImagePath("/uploads/product_images/" + fileName);
		productRepository.save(product);
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Delete a product by ID
	
	public void deleteProductById(Long id) {
		productRepository.deleteById(id); // Deletes the product with the specified ID
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Find products by category ID
	
	public List<Product> findByCategoryId(Long categoryId) {
		return productRepository.findByCategoryId(categoryId); // Returns a list of products belonging to the specified category														
	}
// ---------------------------------------------------------------------------------------------------------------------------
}