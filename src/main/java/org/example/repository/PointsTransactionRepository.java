package org.example.repository;

import org.example.entity.PointsTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointsTransactionRepository extends JpaRepository<PointsTransaction, Long> {
    List<PointsTransaction> findTop10ByCustomerIdOrderByCreateTimeDesc(Long customerId);
    List<PointsTransaction> findByCustomerId(Long customerId);
}
