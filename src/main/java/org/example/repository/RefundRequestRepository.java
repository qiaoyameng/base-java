package org.example.repository;

import org.example.entity.RefundRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefundRequestRepository extends JpaRepository<RefundRequest, Long> {
    Optional<RefundRequest> findByOrderId(Long orderId);
    List<RefundRequest> findByCustomerId(Long customerId);
    List<RefundRequest> findByStatus(String status);
}
