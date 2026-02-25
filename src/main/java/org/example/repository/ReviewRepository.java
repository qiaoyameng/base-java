package org.example.repository;

import org.example.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByOrderId(Long orderId);
    List<Review> findByCustomerId(Long customerId);
    List<Review> findByStoreIdAndVisible(Long storeId, Boolean visible);
    List<Review> findByServiceIdAndVisible(Long serviceId, Boolean visible);
    List<Review> findByStoreId(Long storeId);
}
