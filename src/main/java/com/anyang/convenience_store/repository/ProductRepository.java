package com.anyang.convenience_store.repository;

import com.anyang.convenience_store.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByNameContaining(String keyword);
    Optional<Product> findByProductId(Integer id);
}