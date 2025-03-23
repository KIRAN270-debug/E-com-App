package com.ks.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.ks.entity.User;
import com.ks.entity.Admin;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L; // Add this line to fix the warning // remove this line
														// CutomUSerDetails class shows a waring i want to remove
														// that,so added this line not part of our code,with or without
														// this line the code works.

	private String email;
	private String password;
	private String role;

	// Constructor for User entity
	public CustomUserDetails(User user) {
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.role = user.getRole();
	}

	// Constructor for Admin entity
	public CustomUserDetails(Admin admin) {
		this.email = admin.getEmail();
		this.password = admin.getPassword();
		this.role = admin.getRole();
	}

// ---------------------------------------------------------------------------------------------------------------------------

	// Returns the authorities (roles) granted to the user

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(role));
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Returns the password used to authenticate the user

	@Override
	public String getPassword() {
		return password;
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Returns the username (email) used to authenticate the user

	@Override
	public String getUsername() {
		return email;
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Indicates whether the user's account has expired

	@Override
	public boolean isAccountNonExpired() {
		return true; // Account never expires
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Indicates whether the user is locked or unlocked

	@Override
	public boolean isAccountNonLocked() {
		return true; // Account is never locked
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Indicates whether the user's credentials (password) have expired

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // Credentials never expire
	}
// ---------------------------------------------------------------------------------------------------------------------------

	// Indicates whether the user is enabled or disabled

	@Override
	public boolean isEnabled() {
		return true; // User is always enabled
	}
// ---------------------------------------------------------------------------------------------------------------------------
}