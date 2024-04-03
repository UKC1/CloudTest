package com.anyang.convenience.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anyang.convenience.model.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findByNameContaining(String keyword);

	Optional<Product> findByProductId(Integer id);
}