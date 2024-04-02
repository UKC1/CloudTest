package com.anyang.convenience_store.service;

import com.anyang.convenience_store.model.entity.Product;
import com.anyang.convenience_store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {



    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    void findProductByName() {
        // Given
        String name = "빵";
        Product mockProduct = new Product();
        when(productRepository.findByNameContaining(name)).thenReturn(Collections.singletonList(mockProduct));

        // When
        List<Product> products = productService.findProductByName(name);

        // Then
        assertThat(products).isNotEmpty();
        verify(productRepository).findByNameContaining(name);
    }

    @Test
    void read() {
        Integer id = 1;
        Product mockProduct = new Product(); // Set necessary fields if needed
        when(productRepository.findById(id)).thenReturn(Optional.of(mockProduct));

        // When
        Product product = productService.read(id);

        // Then
        assertThat(product).isNotNull();
        verify(productRepository).findById(id);
    }

    @Test
    void update() {
        // Given
        Product existingProduct = new Product(); // Set necessary fields, including ID and quantity
        existingProduct.setProductId(1);
        existingProduct.setQuantity(10);

        Product updatedProduct = new Product();
        updatedProduct.setProductId(1);
        updatedProduct.setQuantity(5); // Assuming this is the quantity to subtract

        when(productRepository.findById(existingProduct.getProductId())).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        // When
        Product result = productService.update(updatedProduct);

        // Then
        assertThat(result.getQuantity()).isEqualTo(5); // Check the quantity has been updated
        verify(productRepository).save(any(Product.class)); // Verify save method was called
    }

    @Test
    void rollBack() {
        // Given
        Product existingProduct = new Product(); // Set necessary fields, including ID and quantity
        existingProduct.setProductId(1);
        existingProduct.setQuantity(5);

        Integer backCount = 5;

        when(productRepository.findById(existingProduct.getProductId())).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        // When
        Product result = productService.rollBack(existingProduct, backCount);

        // Then
        assertThat(result.getQuantity()).isEqualTo(10); // Check the quantity has been rolled back correctly
        verify(productRepository).save(any(Product.class)); // Verify save method was called
    }
}