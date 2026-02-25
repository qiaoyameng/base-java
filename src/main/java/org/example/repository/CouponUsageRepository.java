package org.example.repository;

import org.example.entity.CouponUsage;
import org.example.enums.CouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {
    List<CouponUsage> findByCustomerIdAndStatus(Long customerId, CouponStatus status);
    List<CouponUsage> findByCustomerId(Long customerId);
    Optional<CouponUsage> findByOrderNo(String orderNo);
    Long countByCouponIdAndCustomerId(Long couponId, Long customerId);
}
