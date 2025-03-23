package com.ks.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data // Combines @Getter, @Setter, @ToString, @EqualsAndHashCode, and @RequiredArgsConstructor
@NoArgsConstructor // Generates a no-args constructor
public class Product {
//---------------------------------------------------------------------------------------------------------------------------
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Not included in the custom constructor
//---------------------------------------------------------------------------------------------------------------------------
    
    @Column(length = 500)
    private String title;
//---------------------------------------------------------------------------------------------------------------------------
    
    @Column(length = 5000)
    private String description;
//---------------------------------------------------------------------------------------------------------------------------
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
//---------------------------------------------------------------------------------------------------------------------------
    
    private Double price;
//---------------------------------------------------------------------------------------------------------------------------
    
    private int stock;
//---------------------------------------------------------------------------------------------------------------------------
    
    private String imagePath; // Stores the path of the uploaded image
//---------------------------------------------------------------------------------------------------------------------------
    
   // Custom constructor without 'id'
    public Product(String title, String description, Category category, Double price, int stock, String imagePath) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.imagePath = imagePath;
    }
//---------------------------------------------------------------------------------------------------------------------------
}