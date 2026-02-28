package org.example.repository;

import org.example.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByProductIdAndDeletedFalse(Long productId, Pageable pageable);

    Page<Review> findByCustomerIdAndDeletedFalse(Long customerId, Pageable pageable);

    List<Review> findByProductIdAndVerifiedTrueAndDeletedFalse(Long productId);

    Optional<Review> findByOrderIdAndProductIdAndDeletedFalse(Long orderId, Long productId);

    @Modifying
    @Query("UPDATE Review r SET r.likeCount = r.likeCount + 1 WHERE r.id = :reviewId")
    int incrementLikeCount(@Param("reviewId") Long reviewId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.productId = :productId AND r.deleted = false")
    Double calculateAverageRating(@Param("productId") Long productId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.productId = :productId AND r.deleted = false")
    Long countByProductId(@Param("productId") Long productId);
}
