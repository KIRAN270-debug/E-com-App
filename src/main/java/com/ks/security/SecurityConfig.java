package com.ks.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Marks this class as a configuration class for Spring
@EnableWebSecurity // Enables Spring Security's web security features
@EnableMethodSecurity // Enables method-level security (e.g., @PreAuthorize)
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService userDetailsService; // Custom service for loading user details

// ---------------------------------------------------------------------------------------------------------------------------

	// Configures the security filter chain for HTTP requests
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/register", "/login","/forgot-password", "/css/**", "/js/**", "/images/**").permitAll() // Public routes

				.requestMatchers("/admin/**").hasRole("ADMIN") // Restrict admin routes to users with ADMIN role

				.requestMatchers("/user/**").hasRole("USER") // Restrict user routes to users with USER role

				.anyRequest().authenticated() // All other routes require authentication

		).formLogin(formLogin -> formLogin.loginPage("/login") // Custom login page

				.defaultSuccessUrl("/", true) // Redirect to home page after successful login

				.failureUrl("/login?error=true") // Redirect to login page with error on failure

				.permitAll() // Allow access to the login page for everyone

		).logout(logout -> logout.logoutSuccessUrl("/login?logout=true") // Redirect to login page after logout

				.permitAll() // Allow access to the logout endpoint for everyone

		).headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()) // Allow iframes from the
																								// same origin

		).userDetailsService(userDetailsService) // Use the custom UserDetailsService for authentication

				.csrf(csrf -> csrf.disable()); // Disable CSRF protection for simplicity

		return http.build(); // Build and return the configured SecurityFilterChain
	}

// ---------------------------------------------------------------------------------------------------------------------------

	// Configures the password encoder to use BCrypt hashing
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // Returns a BCryptPasswordEncoder instance
	}

// ---------------------------------------------------------------------------------------------------------------------------
}