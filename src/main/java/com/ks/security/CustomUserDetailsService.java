package com.ks.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ks.entity.User;
import com.ks.entity.Admin;
import com.ks.repository.UserRepository;
import com.ks.repository.AdminRepository;

@Service // Marks this class as a Spring service component
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository; // Repository for accessing user data
	
	private final AdminRepository adminRepository; // Repository for accessing admin data

// ---------------------------------------------------------------------------------------------------------------------------

	// Constructor for dependency injection
	public CustomUserDetailsService(UserRepository userRepository, AdminRepository adminRepository) {
		this.userRepository = userRepository;
		this.adminRepository = adminRepository;
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Loads a user or admin by their email (username)
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		System.out.println("Loading user by email: " + email);

		// Check in the users table
		User user = userRepository.findByEmail(email).orElse(null);
		if (user != null) {
			System.out.println("User found: " + user.getEmail() + ", Role: " + user.getRole());
			return new CustomUserDetails(user); // Return UserDetails for the user
		}

		// Check in the admin table
		Admin admin = adminRepository.findByEmail(email).orElse(null);
		if (admin != null) {
			System.out.println("Admin found: " + admin.getEmail() + ", Role: " + admin.getRole());
			return new CustomUserDetails(admin); // Return UserDetails for the admin
		}

		// If no user or admin is found, throw an exception
		throw new UsernameNotFoundException("User/Admin not found with email: " + email);
	}
// ---------------------------------------------------------------------------------------------------------------------------
}