package org.example.repository;

import org.example.entity.ConsumptionRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ConsumptionRecordRepository extends JpaRepository<ConsumptionRecord, Long> {
    Page<ConsumptionRecord> findByMemberIdOrderByCreateTimeDesc(Long memberId, Pageable pageable);

    @Query("SELECT SUM(c.amount) FROM ConsumptionRecord c WHERE c.memberId = :memberId")
    BigDecimal sumAmountByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT SUM(c.pointsEarned) FROM ConsumptionRecord c WHERE c.memberId = :memberId")
    Integer sumPointsByMemberId(@Param("memberId") Long memberId);

    List<ConsumptionRecord> findByOrderId(Long orderId);
}
