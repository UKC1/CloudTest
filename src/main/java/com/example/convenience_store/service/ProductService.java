package com.example.convenience_store.service;

import com.example.convenience_store.model.entity.Product;
import com.example.convenience_store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    public List<Product> findProductByName(String name) {
        return productRepository.findByNameContaining(name);
    }
    public Product read(Integer id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            return toProductResponse(product);
        }
        return null;
    }

    private Product toProductResponse(Product product) {
        return Product.builder()
                .store(product.getStore())
                .name(product.getName())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .build();
    }

}