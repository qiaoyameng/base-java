package org.example.repository;

import org.example.entity.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    Page<ProductReview> findByProductIdAndDeletedFalse(Long productId, Pageable pageable);

    Page<ProductReview> findByMemberIdAndDeletedFalse(Long memberId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM ProductReview r WHERE r.productId = :productId AND r.deleted = false")
    Double getAverageRating(@Param("productId") Long productId);

    @Query("SELECT COUNT(r) FROM ProductReview r WHERE r.productId = :productId AND r.deleted = false")
    Long countByProductId(@Param("productId") Long productId);

    List<ProductReview> findByOrderIdAndDeletedFalse(Long orderId);

    boolean existsByMemberIdAndOrderIdAndProductIdAndDeletedFalse(Long memberId, Long orderId, Long productId);
}
