package org.example.repository;

import org.example.entity.MemberBalanceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBalanceLogRepository extends JpaRepository<MemberBalanceLog, Long> {
    Page<MemberBalanceLog> findByMemberIdOrderByCreateTimeDesc(Long memberId, Pageable pageable);
}
