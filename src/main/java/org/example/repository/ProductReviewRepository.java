package org.example.repository;

import org.example.entity.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    Page<ProductReview> findByProductIdAndEnabled(Long productId, Boolean enabled, Pageable pageable);
    List<ProductReview> findByProductIdAndEnabled(Long productId, Boolean enabled);
    Page<ProductReview> findByCustomerId(Long customerId, Pageable pageable);
    List<ProductReview> findByOrderId(Long orderId);
    List<ProductReview> findByOrderItemId(Long orderItemId);
}
