package org.example.repository;

import org.example.entity.Coupon;
import org.example.enums.CouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCouponCode(String couponCode);
    List<Coupon> findByStoreIdAndEnabled(Long storeId, Boolean enabled);
    List<Coupon> findByStatusAndEnabled(CouponStatus status, Boolean enabled);
    List<Coupon> findByEndTimeAfterAndEnabled(LocalDateTime now, Boolean enabled);
}
