package org.example.repository;

import org.example.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String code);

    Page<Coupon> findByActiveTrueOrderByCreateTimeDesc(Pageable pageable);

    @Query("SELECT c FROM Coupon c WHERE c.active = true AND c.startTime <= :now AND c.endTime >= :now AND c.usedQuantity < c.totalQuantity")
    List<Coupon> findAvailableCoupons(LocalDateTime now);

    @Query("SELECT c FROM Coupon c WHERE c.active = true AND c.startTime <= :now AND c.endTime >= :now AND c.usedQuantity < c.totalQuantity AND (c.memberLevel IS NULL OR c.memberLevel = :level)")
    List<Coupon> findAvailableCouponsByMemberLevel(LocalDateTime now, String level);
}
