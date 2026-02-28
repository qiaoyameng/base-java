package org.example.repository;

import org.example.entity.Product;
import org.example.enums.ProductCategory;
import org.example.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Page<Product> findByDeletedFalse(Pageable pageable);

    List<Product> findByCategoryAndStatusAndDeletedFalse(ProductCategory category, ProductStatus status);

    List<Product> findByStatusAndDeletedFalse(ProductStatus status);

    @Query("SELECT p FROM Product p WHERE p.deleted = false " +
            "AND (:name IS NULL OR p.name LIKE %:name%) " +
            "AND (:category IS NULL OR p.category = :category) " +
            "AND (:status IS NULL OR p.status = :status) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> findByConditions(@Param("name") String name,
                                    @Param("category") ProductCategory category,
                                    @Param("status") ProductStatus status,
                                    @Param("minPrice") BigDecimal minPrice,
                                    @Param("maxPrice") BigDecimal maxPrice,
                                    Pageable pageable);

    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock - :quantity WHERE p.id = :productId AND p.stock >= :quantity")
    int deductStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE Product p SET p.salesCount = p.salesCount + :quantity WHERE p.id = :productId")
    int increaseSales(@Param("productId") Long productId, @Param("quantity") Integer quantity);
}
