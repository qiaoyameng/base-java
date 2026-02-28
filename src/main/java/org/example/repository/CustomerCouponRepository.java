package org.example.repository;

import org.example.entity.CustomerCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerCouponRepository extends JpaRepository<CustomerCoupon, Long> {

    Page<CustomerCoupon> findByCustomerIdAndDeletedFalse(Long customerId, Pageable pageable);

    List<CustomerCoupon> findByCustomerIdAndStatusAndDeletedFalse(Long customerId, CustomerCoupon.Status status);

    @Query("SELECT cc FROM CustomerCoupon cc JOIN cc.coupon c WHERE cc.customerId = :customerId AND cc.status = 'UNUSED' AND c.endTime >= :now")
    List<CustomerCoupon> findValidCouponsByCustomerId(@Param("customerId") Long customerId, @Param("now") LocalDateTime now);

    Long countByCustomerIdAndCouponIdAndDeletedFalse(Long customerId, Long couponId);

    Optional<CustomerCoupon> findByIdAndCustomerIdAndStatus(Long id, Long customerId, CustomerCoupon.Status status);
}
