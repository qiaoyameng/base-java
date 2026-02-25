package org.example.order.repository;

import org.example.order.entity.RefundRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefundRecordRepository extends JpaRepository<RefundRecord, Long>, JpaSpecificationExecutor<RefundRecord> {
    
    List<RefundRecord> findByOrderId(Long orderId);
    
    List<RefundRecord> findByMemberId(Long memberId);
    
    List<RefundRecord> findByStatus(Integer status);
    
    Optional<RefundRecord> findByRefundNo(String refundNo);
}
