package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.ProductDTO;
import org.example.entity.Product;
import org.example.enums.ProductCategory;
import org.example.enums.ProductStatus;
import org.example.exception.BusinessException;
import org.example.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setSpecification(dto.getSpecification());
        product.setCategory(dto.getCategory());
        product.setStatus(dto.getStatus());
        product.setDescription(dto.getDescription());
        product.setImageUrl(dto.getImageUrl());
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductDTO dto) {
        Product product = getProductById(id);
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setSpecification(dto.getSpecification());
        product.setCategory(dto.getCategory());
        product.setStatus(dto.getStatus());
        product.setDescription(dto.getDescription());
        product.setImageUrl(dto.getImageUrl());
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("商品不存在"));
    }

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findByDeletedFalse(pageable);
    }

    public Page<Product> getProductsByCategory(ProductCategory category, Pageable pageable) {
        return productRepository.findByCategoryAndDeletedFalse(category, pageable);
    }

    public Page<Product> getProductsByStatus(ProductStatus status, Pageable pageable) {
        return productRepository.findByStatusAndDeletedFalse(status, pageable);
    }

    public Page<Product> searchProducts(String name, Pageable pageable) {
        return productRepository.findByNameContainingAndDeletedFalse(name, pageable);
    }

    @Transactional
    public void updateStock(Long id, Integer quantity) {
        Product product = getProductById(id);
        int newStock = product.getStock() + quantity;
        if (newStock < 0) {
            throw new BusinessException("库存不足");
        }
        product.setStock(newStock);
        if (newStock == 0) {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        }
        productRepository.save(product);
    }

    @Transactional
    public void updateStatus(Long id, ProductStatus status) {
        Product product = getProductById(id);
        product.setStatus(status);
        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setDeleted(true);
        productRepository.save(product);
    }

    public List<Product> getLowStockProducts(Integer threshold) {
        return productRepository.findLowStockProducts(threshold);
    }
}
