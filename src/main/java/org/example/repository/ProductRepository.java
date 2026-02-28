package org.example.repository;

import org.example.entity.Product;
import org.example.enums.ProductCategory;
import org.example.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndDeletedFalse(Long id);

    Page<Product> findByDeletedFalse(Pageable pageable);

    Page<Product> findByCategoryAndDeletedFalse(ProductCategory category, Pageable pageable);

    Page<Product> findByStatusAndDeletedFalse(ProductStatus status, Pageable pageable);

    Page<Product> findByCategoryAndStatusAndDeletedFalse(ProductCategory category, ProductStatus status, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.deleted = false AND p.name LIKE %:name%")
    Page<Product> findByNameContainingAndDeletedFalse(@Param("name") String name, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.deleted = false AND p.stock < :threshold")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);
}
