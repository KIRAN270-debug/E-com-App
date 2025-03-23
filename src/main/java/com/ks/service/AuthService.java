package com.ks.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ks.entity.Admin;
import com.ks.entity.User;
import com.ks.repository.AdminRepository;
import com.ks.repository.UserRepository;

@Service // Marks this class as a Spring service component
public class AuthService {

	@Autowired
	private UserRepository userRepository; // Repository for accessing user data

	@Autowired
	private AdminRepository adminRepository; // Repository for accessing admin data

	@Autowired
	private PasswordEncoder passwordEncoder; // Encoder for hashing and verifying passwords

// ---------------------------------------------------------------------------------------------------------------------------

	// Authenticate a user or admin by email and password
	
	public String authenticate(String email, String password) {
		// Check if the email belongs to a user
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
			return "ROLE_USER"; // Return USER role if authentication succeeds
		}

		// Check if the email belongs to an admin
		Optional<Admin> admin = adminRepository.findByEmail(email);
		if (admin.isPresent() && passwordEncoder.matches(password, admin.get().getPassword())) {
			return "ROLE_ADMIN"; // Return ADMIN role if authentication succeeds
		}

		// Return null if authentication fails
		return null;
	}
// ---------------------------------------------------------------------------------------------------------------------------
}