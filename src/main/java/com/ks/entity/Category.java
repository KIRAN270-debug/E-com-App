package com.ks.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor // Generates an all-args constructor
@Getter // Generates getters for all fields
@Setter // Generates setters for all fields
public class Category {
// ---------------------------------------------------------------------------------------------------------------------------
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
//---------------------------------------------------------------------------------------------------------------------------
	
	private String name;
	private String imagePath; // Stores the path of the uploaded image
//---------------------------------------------------------------------------------------------------------------------------
}