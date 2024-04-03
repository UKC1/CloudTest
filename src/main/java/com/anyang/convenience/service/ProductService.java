package com.anyang.convenience.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anyang.convenience.model.entity.Product;
import com.anyang.convenience.repository.ProductRepository;

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

	public Product update(Product updatedProduct) {
		Product existingProduct = productRepository.findById(updatedProduct.getProductId()).orElse(null);
		if (existingProduct != null) {
			if (existingProduct.getQuantity() - updatedProduct.getQuantity() < 0) {
			} else {
				existingProduct.setQuantity(existingProduct.getQuantity() - updatedProduct.getQuantity());
			}
			Product updated = productRepository.save(existingProduct);
			return toProductResponse(updated);
		}
		return null;
	}

	public Product rollBack(Product backProduct, Integer backCount) {
		Product existingProduct = productRepository.findById(backProduct.getProductId()).orElse(null);
		if (existingProduct != null) {
			existingProduct.setQuantity(existingProduct.getQuantity() + backCount);
			Product rollBack = productRepository.save(existingProduct);
			return toProductResponse(rollBack);
		}
		return null;
	}

	private Product toProductResponse(Product product) {
		return Product.builder()
			.productId(product.getProductId())
			.store(product.getStore())
			.name(product.getName())
			.quantity(product.getQuantity())
			.price(product.getPrice())
			.build();
	}

}