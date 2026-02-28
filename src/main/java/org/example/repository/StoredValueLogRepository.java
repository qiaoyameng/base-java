package org.example.repository;

import org.example.entity.StoredValueLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoredValueLogRepository extends JpaRepository<StoredValueLog, Long> {

    Page<StoredValueLog> findByCustomerId(Long customerId, Pageable pageable);

    List<StoredValueLog> findByCustomerId(Long customerId);

    List<StoredValueLog> findByRelatedOrderId(Long relatedOrderId);

    Page<StoredValueLog> findByCardNo(String cardNo, Pageable pageable);

    Page<StoredValueLog> findByCustomerIdAndType(Long customerId, StoredValueLog.LogType type, Pageable pageable);
}
