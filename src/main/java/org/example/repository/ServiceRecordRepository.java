package org.example.repository;

import org.example.entity.ServiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRecordRepository extends JpaRepository<ServiceRecord, Long> {
    Optional<ServiceRecord> findByOrderId(Long orderId);
    List<ServiceRecord> findByCustomerId(Long customerId);
    List<ServiceRecord> findByStoreId(Long storeId);
}
