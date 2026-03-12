package org.example.repository;

import org.example.entity.MemberCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    Page<MemberCoupon> findByMemberIdOrderByCreateTimeDesc(Long memberId, Pageable pageable);

    List<MemberCoupon> findByMemberIdAndUsedFalse(Long memberId);

    Optional<MemberCoupon> findByMemberIdAndCouponIdAndUsedFalse(Long memberId, Long couponId);

    boolean existsByMemberIdAndCouponId(Long memberId, Long couponId);
}
