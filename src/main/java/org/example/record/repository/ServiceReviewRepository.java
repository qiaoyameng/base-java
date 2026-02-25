package org.example.record.repository;

import org.example.record.entity.ServiceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceReviewRepository extends JpaRepository<ServiceReview, Long>, JpaSpecificationExecutor<ServiceReview> {
    
    List<ServiceReview> findByOrderId(Long orderId);
    
    List<ServiceReview> findByMemberId(Long memberId);
    
    List<ServiceReview> findByStoreId(Long storeId);
    
    List<ServiceReview> findByEmployeeId(Long employeeId);
    
    Optional<ServiceReview> findByOrderIdAndMemberId(Long orderId, Long memberId);
    
    List<ServiceReview> findByStoreIdOrderByCreatedAtDesc(Long storeId);
}
