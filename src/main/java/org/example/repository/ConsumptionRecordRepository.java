package org.example.repository;

import org.example.entity.ConsumptionRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumptionRecordRepository extends JpaRepository<ConsumptionRecord, Long> {

    Page<ConsumptionRecord> findByCustomerIdAndDeletedFalse(Long customerId, Pageable pageable);

    Optional<ConsumptionRecord> findByOrderId(Long orderId);

    @Query("SELECT SUM(c.consumptionAmount) FROM ConsumptionRecord c WHERE c.customerId = :customerId AND c.deleted = false")
    Double sumConsumptionByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT SUM(c.earnedPoints) FROM ConsumptionRecord c WHERE c.customerId = :customerId AND c.deleted = false")
    Integer sumEarnedPointsByCustomerId(@Param("customerId") Long customerId);

    List<ConsumptionRecord> findByCustomerIdAndConsumptionTimeBetweenAndDeletedFalse(
            Long customerId, LocalDateTime startTime, LocalDateTime endTime);
}
