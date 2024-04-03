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
    void findProductByName_ShouldReturnProducts_WhenNameMatches() {
        String name = "빵";
        Product mockProduct = new Product();
        when(productRepository.findByNameContaining(name)).thenReturn(Collections.singletonList(mockProduct));

        List<Product> products = productService.findProductByName(name);

        assertThat(products).isNotEmpty();
        verify(productRepository).findByNameContaining(name);
    }

    @Test
    void read_ShouldReturnProduct_WhenIdExists() {
        Integer id = 1;
        Product mockProduct = new Product();
        when(productRepository.findById(id)).thenReturn(Optional.of(mockProduct));

        Product product = productService.read(id);
        assertThat(product).isNotNull();
        verify(productRepository).findById(id);
    }

    @Test
    void updateQuantity_ShouldUpdateQuantity_WhenSufficient() {
        Product existingProduct = new Product();
        existingProduct.setProductId(1);
        existingProduct.setQuantity(10);

        Product updatedProduct = new Product();
        updatedProduct.setProductId(1);
        updatedProduct.setQuantity(5);

        when(productRepository.findById(existingProduct.getProductId())).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Product result = productService.update(updatedProduct);
        assertThat(result.getQuantity()).isEqualTo(5); // Check the quantity has been updated
        verify(productRepository).save(any(Product.class)); // Verify save method was called
    }

    @Test
    void rollbackQuantity_ShouldIncreaseQuantity() {
        Product existingProduct = new Product();
        existingProduct.setProductId(1);
        existingProduct.setQuantity(5);
        Integer backCount = 5;

        when(productRepository.findById(existingProduct.getProductId())).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Product result = productService.rollBack(existingProduct, backCount);
        assertThat(result.getQuantity()).isEqualTo(10);
        verify(productRepository).save(any(Product.class));
    }
}