package com.ks.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
//---------------------------------------------------------------------------------------------------------------------------

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
//---------------------------------------------------------------------------------------------------------------------------

	@NotBlank(message = "Name cannot be empty")
	private String name;
//---------------------------------------------------------------------------------------------------------------------------

	@NotBlank(message = "Email cannot be empty")
	@Email(message = "Invalid email format")
	@Column(unique = true)
	private String email;
//---------------------------------------------------------------------------------------------------------------------------

	@NotBlank(message = "Password cannot be empty")
	private String password;
//---------------------------------------------------------------------------------------------------------------------------

	@NotBlank(message = "Mobile number cannot be empty")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Mobile number must be a valid 10-digit number")
	private String mobileNumber;
//---------------------------------------------------------------------------------------------------------------------------

	@NotBlank(message = "Address cannot be empty")
	private String address;
//---------------------------------------------------------------------------------------------------------------------------

	@NotBlank(message = "City cannot be empty")
	private String city;
//---------------------------------------------------------------------------------------------------------------------------

	@NotBlank(message = "State cannot be empty")
	private String state;
//---------------------------------------------------------------------------------------------------------------------------

	@NotBlank(message = "Pincode cannot be empty")
	@Pattern(regexp = "\\d{6}", message = "Pincode must be a 6-digit number")
	private String pincode;
//---------------------------------------------------------------------------------------------------------------------------

	private String image;
//---------------------------------------------------------------------------------------------------------------------------

	@Column(nullable = false)
	private String role = "ROLE_USER"; // Default role value
//---------------------------------------------------------------------------------------------------------------------------

	@Transient
	private String confirmPassword;
// ---------------------------------------------------------------------------------------------------------------------------
}