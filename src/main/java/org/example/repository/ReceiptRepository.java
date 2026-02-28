package org.example.repository;

import org.example.entity.Receipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Optional<Receipt> findByReceiptNo(String receiptNo);
    Optional<Receipt> findByOrderNo(String orderNo);
    Optional<Receipt> findByOrderId(Long orderId);
    Page<Receipt> findByCustomerId(Long customerId, Pageable pageable);
}
