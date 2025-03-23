package com.ks.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ks.entity.User;
import com.ks.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service // Marks this class as a Spring service component
public class UserService {

	@Autowired
	private UserRepository userRepository; // Repository for accessing user data

	@Autowired
	private PasswordEncoder passwordEncoder; // Encoder for hashing passwords

// ---------------------------------------------------------------------------------------------------------------------------

	// Directory where profile images will be stored
	
	private static final String UPLOAD_DIR = "uploads/profile_images/";

// ---------------------------------------------------------------------------------------------------------------------------

	// Save a new user with a profile image
	
	@Transactional // Ensures the method runs within a transactional context
	public void saveUser(User user, MultipartFile image) throws IOException {

		// Validate if email already exists
		Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
		if (existingUser.isPresent()) {
			System.out.println("Email is already registered: " + user.getEmail());
			throw new IllegalStateException("Email is already registered");
		} else {
			System.out.println("Email is unique. Proceeding with registration.");
		}

		// Validate password and confirm password (compare plain text passwords)
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			System.out.println("Passwords do not match.");
			throw new IllegalArgumentException("Passwords do not match");
		} else {
			System.out.println("Passwords match. Proceeding with registration.");
		}

		// Encrypt the password using BCrypt (after validation)
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Set role directly in the backend
		user.setRole("ROLE_USER");

		// Save profile image if provided
		if (image != null && !image.isEmpty()) {
			System.out.println("Uploading profile image...");
			String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
			Path filePath = Paths.get(UPLOAD_DIR + fileName);

			// Ensure the directory exists before saving the file
			Files.createDirectories(filePath.getParent());

			// Save the image file to the specified directory
			Files.write(filePath, image.getBytes());

			// Set the image path in the user object
			user.setImage("/" + UPLOAD_DIR + fileName);
		} else {
			System.out.println("No profile image provided. Using default image.");
			user.setImage("/" + UPLOAD_DIR + "default.jpg"); // Set a default image if none is provided
		}

		// Save the user to the database
		userRepository.save(user);
		System.out.println("User saved successfully!");
	}
// ---------------------------------------------------------------------------------------------------------------------------
}