package org.example.record.repository;

import org.example.record.entity.ServiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRecordRepository extends JpaRepository<ServiceRecord, Long>, JpaSpecificationExecutor<ServiceRecord> {
    
    List<ServiceRecord> findByOrderId(Long orderId);
    
    List<ServiceRecord> findByMemberId(Long memberId);
    
    List<ServiceRecord> findByStoreId(Long storeId);
    
    List<ServiceRecord> findByEmployeeId(Long employeeId);
    
    Optional<ServiceRecord> findByOrderItemId(Long orderItemId);
}
