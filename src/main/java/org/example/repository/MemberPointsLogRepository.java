package org.example.repository;

import org.example.entity.MemberPointsLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPointsLogRepository extends JpaRepository<MemberPointsLog, Long> {
    Page<MemberPointsLog> findByMemberIdOrderByCreateTimeDesc(Long memberId, Pageable pageable);
}
