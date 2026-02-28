package org.example.repository;

import org.example.entity.Coupon;
import org.example.entity.CustomerCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerCouponRepository extends JpaRepository<CustomerCoupon, Long> {
    List<CustomerCoupon> findByCustomerId(Long customerId);
    List<CustomerCoupon> findByCustomerIdAndStatus(Long customerId, CustomerCoupon.CustomerCouponStatus status);
    Optional<CustomerCoupon> findByCustomerIdAndCouponId(Long customerId, Long couponId);
    int countByCustomerIdAndCouponId(Long customerId, Long couponId);

    @Query("SELECT c FROM Coupon c WHERE c.id = (SELECT cc.couponId FROM CustomerCoupon cc WHERE cc.id = :customerCouponId)")
    Coupon findCouponById(@Param("customerCouponId") Long customerCouponId);

    @Query("SELECT cc FROM CustomerCoupon cc WHERE cc.status = 'AVAILABLE' AND cc.validTo < :now")
    List<CustomerCoupon> findExpiredCoupons(LocalDateTime now);
}
