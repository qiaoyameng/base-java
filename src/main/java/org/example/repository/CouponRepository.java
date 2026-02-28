package org.example.repository;

import org.example.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>, JpaSpecificationExecutor<Coupon> {

    Optional<Coupon> findByCouponCode(String couponCode);

    List<Coupon> findByEnabledAndStatus(Boolean enabled, Coupon.CouponStatus status);

    Page<Coupon> findByEnabled(Boolean enabled, Pageable pageable);

    @Query("SELECT c FROM Coupon c WHERE c.enabled = true " +
           "AND c.status = 'ACTIVE' " +
           "AND c.validFrom <= :now AND c.validTo >= :now " +
           "AND (c.memberLevelRequired IS NULL OR c.memberLevelRequired IN :levels) " +
           "AND c.totalCount > c.usedCount")
    List<Coupon> findAvailableCoupons(@Param("now") LocalDateTime now, @Param("levels") List<String> levels);

    @Query("SELECT c FROM Coupon c WHERE c.enabled = true AND c.status = 'ACTIVE' AND c.validTo < :now")
    List<Coupon> findExpiredCoupons(@Param("now") LocalDateTime now);

    @Query("SELECT c FROM Coupon c WHERE c.enabled = true " +
           "AND c.status = 'ACTIVE' " +
           "AND c.totalCount > c.usedCount " +
           "ORDER BY c.createTime DESC")
    List<Coupon> findActiveCoupons(Pageable pageable);
}
