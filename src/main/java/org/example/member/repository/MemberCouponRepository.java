package org.example.member.repository;

import org.example.common.enums.CouponStatus;
import org.example.member.entity.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long>, JpaSpecificationExecutor<MemberCoupon> {
    
    List<MemberCoupon> findByMemberId(Long memberId);
    
    List<MemberCoupon> findByMemberIdAndStatus(Long memberId, CouponStatus status);
    
    Optional<MemberCoupon> findByIdAndMemberId(Long id, Long memberId);
    
    List<MemberCoupon> findByMemberIdAndStatusAndExpireTimeAfter(Long memberId, CouponStatus status, LocalDateTime time);
}
