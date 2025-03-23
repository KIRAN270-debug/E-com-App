package com.ks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.ks.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	List<Product> findByCategoryId(Long categoryId);


}
