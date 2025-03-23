package com.ks.service;

import com.ks.entity.Category;
import com.ks.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service // Marks this class as a Spring service component
@Transactional // Ensures that all methods are executed within a transactional context
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository; // Repository for accessing category data

// ---------------------------------------------------------------------------------------------------------------------------

	// Get all categories
	
	public List<Category> getAllCategory() {
		return categoryRepository.findAll(); // Returns a list of all categories
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Find a category by ID
	
	public Optional<Category> findById(Long id) {
		return categoryRepository.findById(id); // Returns an Optional containing the category if found
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Save a new category with an image
	
	public void saveCategory(String name, MultipartFile image) throws IOException {
		if (image.isEmpty()) {
			// If the image is not provided, throw an exception
			throw new IllegalArgumentException("Image file is required!");
		}

		// Create a unique file name using the current timestamp and original file name
		String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
		Path filePath = Paths.get("uploads/category_images/" + fileName); // Path where the image will be saved

		// Ensure the directory exists before saving the file
		Files.createDirectories(filePath.getParent());

		// Save the image file to the specified directory
		Files.write(filePath, image.getBytes());

		// Create a new Category object with the data, including the image URL
		Category category = new Category();
		category.setName(name);
		category.setImagePath("/uploads/category_images/" + fileName); // Image path for front-end reference

		// Save the category to the database
		categoryRepository.save(category);
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Update an existing category
	
	public void updateCategory(Category category) {
		categoryRepository.save(category); // Updates the category in the database
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Delete a category by ID
	
	public void deleteCategory(Long id) {
		categoryRepository.deleteById(id); // Deletes the category with the specified ID
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Get a category by ID or throw an exception if not found
	
	public Category getCategoryById(Long id) {
		return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found")); // Throws exception if category not found
																														
	}
// ---------------------------------------------------------------------------------------------------------------------------
}