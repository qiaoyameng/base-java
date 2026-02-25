package org.example.member.repository;

import org.example.member.entity.PointRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRecordRepository extends JpaRepository<PointRecord, Long>, JpaSpecificationExecutor<PointRecord> {
    
    List<PointRecord> findByMemberIdOrderByCreatedAtDesc(Long memberId);
}
