package org.example.member.repository;

import org.example.common.enums.CouponType;
import org.example.member.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>, JpaSpecificationExecutor<Coupon> {
    
    List<Coupon> findByCouponType(CouponType couponType);
    
    Optional<Coupon> findByCouponCode(String couponCode);
    
    List<Coupon> findByActiveTrue();
}
