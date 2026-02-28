package org.example.repository;

import org.example.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    List<Coupon> findByActiveTrueAndDisplayOnHomeTrueAndStartTimeBeforeAndEndTimeAfterOrderBySortOrderAsc(
            LocalDateTime now1, LocalDateTime now2);

    Page<Coupon> findByActiveTrueAndStartTimeBeforeAndEndTimeAfterOrderBySortOrderAsc(
            LocalDateTime now1, LocalDateTime now2, Pageable pageable);

    @Modifying
    @Query("UPDATE Coupon c SET c.remainingQuantity = c.remainingQuantity - 1 WHERE c.id = :id AND c.remainingQuantity > 0")
    int deductRemainingQuantity(@Param("id") Long id);

    @Query("SELECT c FROM Coupon c WHERE c.active = true AND c.startTime <= :now AND c.endTime >= :now AND c.remainingQuantity > 0")
    List<Coupon> findAvailableCoupons(@Param("now") LocalDateTime now);
}
