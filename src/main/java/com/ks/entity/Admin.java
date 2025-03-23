package com.ks.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "admin")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
//---------------------------------------------------------------------------------------------------------------------------
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
//---------------------------------------------------------------------------------------------------------------------------
	
	@NotBlank(message = "Name cannot be empty")
	@Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
	@Column(nullable = false)
	private String name;
//---------------------------------------------------------------------------------------------------------------------------
	
	@NotBlank(message = "Email cannot be empty")
	@Email(message = "Invalid email format")
	@Column(nullable = false, unique = true)
	private String email;
//---------------------------------------------------------------------------------------------------------------------------
	
	@Column(nullable = false)
	private String password;
//---------------------------------------------------------------------------------------------------------------------------
	
	@Column(nullable = false)
	private String role = "ROLE_ADMIN"; // Default role for admins
//---------------------------------------------------------------------------------------------------------------------------
}
