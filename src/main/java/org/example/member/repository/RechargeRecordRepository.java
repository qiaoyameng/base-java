package org.example.member.repository;

import org.example.member.entity.RechargeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RechargeRecordRepository extends JpaRepository<RechargeRecord, Long>, JpaSpecificationExecutor<RechargeRecord> {
    
    List<RechargeRecord> findByMemberIdOrderByCreatedAtDesc(Long memberId);
}
