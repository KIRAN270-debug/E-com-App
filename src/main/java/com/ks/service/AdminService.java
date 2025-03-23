package com.ks.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ks.entity.Admin;
import com.ks.repository.AdminRepository;

@Service // Marks this class as a Spring service component
public class AdminService {

    @Autowired
    private AdminRepository adminRepository; // Repository for accessing admin data

    @Autowired
    private PasswordEncoder passwordEncoder; // Encoder for hashing passwords

// ---------------------------------------------------------------------------------------------------------------------------

    // Create a new admin securely
    
    public void saveAdmin(Admin admin) {
        // Validate if email already exists
        Optional<Admin> existingAdmin = adminRepository.findByEmail(admin.getEmail());
        if (existingAdmin.isPresent()) {
            System.out.println("Email is already registered: " + admin.getEmail());
            throw new IllegalStateException("Email is already registered");
        } else {
            System.out.println("Email is unique. Proceeding with registration.");
        }

        // Encrypt the password using BCrypt
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        // Set role directly in the backend
        admin.setRole("ROLE_ADMIN");

        // Save admin in the database
        adminRepository.save(admin);
        System.out.println("Admin saved successfully!");
    }
// ---------------------------------------------------------------------------------------------------------------------------

    // Get all admins
    
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll(); // Returns a list of all admins
    }
// ---------------------------------------------------------------------------------------------------------------------------

    // Find an admin by ID
    
    public Admin findById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id)); // Throws exception if admin not found
    }
// ---------------------------------------------------------------------------------------------------------------------------

    // Delete an admin by ID
    
    public void deleteAdmin(Long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id); // Deletes the admin if it exists
            System.out.println("Admin deleted successfully!");
        } else {
            throw new RuntimeException("Admin not found with id: " + id); // Throws exception if admin not found
        }
    }
// ---------------------------------------------------------------------------------------------------------------------------

    // Update an admin securely
    
    public Admin updateAdmin(Long id, Admin updatedAdmin) {
        // Find the existing admin by ID
        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));

        // Update name and email
        existingAdmin.setName(updatedAdmin.getName());
        existingAdmin.setEmail(updatedAdmin.getEmail());

        // Only update password if it's not empty
        if (updatedAdmin.getPassword() != null && !updatedAdmin.getPassword().isEmpty()) {
            existingAdmin.setPassword(passwordEncoder.encode(updatedAdmin.getPassword()));
        }

        // Save the updated admin in the database
        Admin savedAdmin = adminRepository.save(existingAdmin);
        System.out.println("Admin updated successfully!");
        return savedAdmin;
    }
// ---------------------------------------------------------------------------------------------------------------------------
}