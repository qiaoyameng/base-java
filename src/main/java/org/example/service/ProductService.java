package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Category;
import org.example.entity.Product;
import org.example.repository.CategoryRepository;
import org.example.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Page<Product> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return productRepository.findAll(pageable);
    }

    public Page<Product> findByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return productRepository.findByCategoryIdAndStatus(categoryId, Product.ProductStatus.ON_SALE, pageable);
    }

    public Page<Product> findOnSale(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return productRepository.findByStatus(Product.ProductStatus.ON_SALE, pageable);
    }

    public Page<Product> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return productRepository.searchByKeyword(keyword, pageable);
    }

    public List<Product> findTopSales(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return productRepository.findTopSales(pageable);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Transactional
    public Product save(Product product) {
        if (product.getCategoryId() != null) {
            Category category = categoryRepository.findById(product.getCategoryId()).orElse(null);
            product.setCategory(category);
        }
        if (product.getStatus() == null) {
            product.setStatus(Product.ProductStatus.OFF_SALE);
        }
        return productRepository.save(product);
    }

    @Transactional
    public Product update(Long id, Product product) {
        Product existing = findById(id);
        if (existing == null) {
            return null;
        }
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setOriginalPrice(product.getOriginalPrice());
        existing.setStock(product.getStock());
        existing.setSpec(product.getSpec());
        existing.setImages(product.getImages());
        existing.setDescription(product.getDescription());
        existing.setDetail(product.getDetail());
        existing.setStatus(product.getStatus());
        existing.setSortOrder(product.getSortOrder());
        if (product.getCategoryId() != null && !product.getCategoryId().equals(existing.getCategoryId())) {
            Category category = categoryRepository.findById(product.getCategoryId()).orElse(null);
            existing.setCategory(category);
            existing.setCategoryId(product.getCategoryId());
        }
        return productRepository.save(existing);
    }

    @Transactional
    public void updateStatus(Long id, Product.ProductStatus status) {
        Product product = findById(id);
        if (product != null) {
            product.setStatus(status);
            productRepository.save(product);
        }
    }

    @Transactional
    public void updateStock(Long id, Integer stock) {
        Product product = findById(id);
        if (product != null) {
            product.setStock(stock);
            productRepository.save(product);
        }
    }

    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public boolean decreaseStock(Long id, int quantity) {
        Product product = findById(id);
        if (product == null || product.getStock() < quantity) {
            return false;
        }
        product.setStock(product.getStock() - quantity);
        product.setSalesCount(product.getSalesCount() + quantity);
        productRepository.save(product);
        return true;
    }

    @Transactional
    public void increaseStock(Long id, int quantity) {
        Product product = findById(id);
        if (product != null) {
            product.setStock(product.getStock() + quantity);
            product.setSalesCount(Math.max(0, product.getSalesCount() - quantity));
            productRepository.save(product);
        }
    }
}
