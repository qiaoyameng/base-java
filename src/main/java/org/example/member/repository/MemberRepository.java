package org.example.member.repository;

import org.example.common.enums.MemberLevel;
import org.example.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
    
    Optional<Member> findByUserId(Long userId);
    
    Optional<Member> findByPhone(String phone);
    
    List<Member> findByLevel(MemberLevel level);
    
    List<Member> findByStatus(Integer status);
}
