package org.example.repository;

import org.example.entity.TransactionRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long> {
    Optional<TransactionRecord> findByTransactionNo(String transactionNo);
    List<TransactionRecord> findByCustomerId(Long customerId);
    Page<TransactionRecord> findByCustomerId(Long customerId, Pageable pageable);
    List<TransactionRecord> findByOrderId(Long orderId);
    Optional<TransactionRecord> findByOrderNo(String orderNo);
}
