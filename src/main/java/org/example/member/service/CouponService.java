package org.example.member.service;

import org.example.common.enums.CouponStatus;
import org.example.common.enums.CouponType;
import org.example.member.entity.Coupon;
import org.example.member.entity.MemberCoupon;
import org.example.member.repository.CouponRepository;
import org.example.member.repository.MemberCouponRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CouponService {
    
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    public CouponService(CouponRepository couponRepository, MemberCouponRepository memberCouponRepository) {
        this.couponRepository = couponRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    public List<Coupon> findAll() {
        return couponRepository.findAll();
    }

    public Page<Coupon> findAll(Pageable pageable) {
        return couponRepository.findAll(pageable);
    }

    public Optional<Coupon> findById(Long id) {
        return couponRepository.findById(id);
    }

    public Optional<Coupon> findByCouponCode(String couponCode) {
        return couponRepository.findByCouponCode(couponCode);
    }

    public List<Coupon> findByCouponType(CouponType couponType) {
        return couponRepository.findByCouponType(couponType);
    }

    public List<Coupon> findActiveCoupons() {
        return couponRepository.findByActiveTrue();
    }

    @Transactional
    public Coupon save(Coupon coupon) {
        if (coupon.getCouponCode() == null) {
            coupon.setCouponCode(generateCouponCode());
        }
        return couponRepository.save(coupon);
    }

    @Transactional
    public Coupon createCoupon(String couponName, CouponType couponType, BigDecimal discountValue, 
                              BigDecimal minAmount, BigDecimal maxDiscount, Integer totalQuantity, 
                              LocalDateTime startTime, LocalDateTime endTime, Integer validDays, 
                              String description) {
        Coupon coupon = new Coupon();
        coupon.setCouponName(couponName);
        coupon.setCouponType(couponType);
        coupon.setDiscountValue(discountValue);
        coupon.setMinAmount(minAmount);
        coupon.setMaxDiscount(maxDiscount);
        coupon.setTotalQuantity(totalQuantity);
        coupon.setStartTime(startTime);
        coupon.setEndTime(endTime);
        coupon.setValidDays(validDays);
        coupon.setDescription(description);
        coupon.setCouponCode(generateCouponCode());
        return couponRepository.save(coupon);
    }

    @Transactional
    public boolean delete(Long id) {
        if (couponRepository.existsById(id)) {
            couponRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateStatus(Long id, boolean active) {
        return couponRepository.findById(id).map(coupon -> {
            coupon.setActive(active);
            couponRepository.save(coupon);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean issueCouponToMember(Long couponId, Long memberId) {
        Coupon coupon = couponRepository.findById(couponId).orElse(null);
        if (coupon == null || !coupon.getActive()) {
            return false;
        }
        
        if (coupon.getUsedQuantity() >= coupon.getTotalQuantity()) {
            return false;
        }
        
        // 检查会员是否已经领取过该优惠券
        List<MemberCoupon> existingCoupons = memberCouponRepository.findByMemberId(memberId);
        for (MemberCoupon existing : existingCoupons) {
            if (existing.getCouponId().equals(couponId)) {
                return false;
            }
        }
        
        MemberCoupon memberCoupon = new MemberCoupon();
        memberCoupon.setCouponId(couponId);
        memberCoupon.setMemberId(memberId);
        memberCoupon.setStatus(CouponStatus.AVAILABLE);
        memberCoupon.setReceiveTime(LocalDateTime.now());
        
        if (coupon.getValidDays() != null) {
            memberCoupon.setExpireTime(LocalDateTime.now().plusDays(coupon.getValidDays()));
        } else {
            memberCoupon.setExpireTime(coupon.getEndTime());
        }
        
        memberCouponRepository.save(memberCoupon);
        
        coupon.setUsedQuantity(coupon.getUsedQuantity() + 1);
        couponRepository.save(coupon);
        
        return true;
    }

    @Transactional
    public boolean useCoupon(Long memberCouponId, Long orderId, BigDecimal orderAmount) {
        MemberCoupon memberCoupon = memberCouponRepository.findById(memberCouponId).orElse(null);
        if (memberCoupon == null || memberCoupon.getStatus() != CouponStatus.AVAILABLE) {
            return false;
        }
        
        if (memberCoupon.getExpireTime().isBefore(LocalDateTime.now())) {
            memberCoupon.setStatus(CouponStatus.EXPIRED);
            memberCouponRepository.save(memberCoupon);
            return false;
        }
        
        Coupon coupon = couponRepository.findById(memberCoupon.getCouponId()).orElse(null);
        if (coupon == null || !coupon.getActive()) {
            return false;
        }
        
        if (orderAmount.compareTo(coupon.getMinAmount()) < 0) {
            return false;
        }
        
        memberCoupon.setStatus(CouponStatus.USED);
        memberCoupon.setUseTime(LocalDateTime.now());
        memberCoupon.setOrderId(orderId);
        memberCouponRepository.save(memberCoupon);
        
        return true;
    }

    public BigDecimal calculateDiscount(Long memberCouponId, BigDecimal orderAmount) {
        MemberCoupon memberCoupon = memberCouponRepository.findById(memberCouponId).orElse(null);
        if (memberCoupon == null || memberCoupon.getStatus() != CouponStatus.AVAILABLE) {
            return BigDecimal.ZERO;
        }
        
        if (memberCoupon.getExpireTime().isBefore(LocalDateTime.now())) {
            return BigDecimal.ZERO;
        }
        
        Coupon coupon = couponRepository.findById(memberCoupon.getCouponId()).orElse(null);
        if (coupon == null || !coupon.getActive()) {
            return BigDecimal.ZERO;
        }
        
        if (orderAmount.compareTo(coupon.getMinAmount()) < 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal discount = BigDecimal.ZERO;
        CouponType type = coupon.getCouponType();
        switch (type) {
            case FULL_MINUS:
                discount = coupon.getDiscountValue();
                break;
            case DISCOUNT:
                discount = orderAmount.multiply(coupon.getDiscountValue()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                break;
            case FREE_SHIPPING:
                discount = new BigDecimal(15); // 假设运费15元
                break;
        }
        
        if (coupon.getMaxDiscount() != null && discount.compareTo(coupon.getMaxDiscount()) > 0) {
            discount = coupon.getMaxDiscount();
        }
        
        return discount;
    }

    public List<MemberCoupon> getMemberCoupons(Long memberId, CouponStatus status) {
        if (status == null) {
            return memberCouponRepository.findByMemberId(memberId);
        }
        return memberCouponRepository.findByMemberIdAndStatus(memberId, status);
    }

    public List<MemberCoupon> getAvailableCoupons(Long memberId) {
        return memberCouponRepository.findByMemberIdAndStatusAndExpireTimeAfter(memberId, CouponStatus.AVAILABLE, LocalDateTime.now());
    }

    @Transactional
    public void checkAndExpireCoupons() {
        LocalDateTime now = LocalDateTime.now();
        List<MemberCoupon> memberCoupons = memberCouponRepository.findByMemberIdAndStatusAndExpireTimeAfter(0L, CouponStatus.AVAILABLE, now.minusYears(10));
        for (MemberCoupon coupon : memberCoupons) {
            if (coupon.getExpireTime() != null && coupon.getExpireTime().isBefore(now)) {
                coupon.setStatus(CouponStatus.EXPIRED);
                memberCouponRepository.save(coupon);
            }
        }
    }

    // 添加CouponController需要的方法
    public Optional<Coupon> findByCode(String code) {
        return couponRepository.findByCouponCode(code);
    }

    public List<Coupon> findByType(CouponType type) {
        return couponRepository.findByCouponType(type);
    }

    public Coupon create(Coupon coupon) {
        return save(coupon);
    }

    public Coupon update(Long id, Coupon coupon) {
        Optional<Coupon> existing = couponRepository.findById(id);
        if (existing.isPresent()) {
            Coupon updated = existing.get();
            updated.setCouponName(coupon.getCouponName());
            updated.setCouponType(coupon.getCouponType());
            updated.setDiscountValue(coupon.getDiscountValue());
            updated.setMinAmount(coupon.getMinAmount());
            updated.setMaxDiscount(coupon.getMaxDiscount());
            updated.setTotalQuantity(coupon.getTotalQuantity());
            updated.setStartTime(coupon.getStartTime());
            updated.setEndTime(coupon.getEndTime());
            updated.setValidDays(coupon.getValidDays());
            updated.setDescription(coupon.getDescription());
            updated.setActive(coupon.getActive());
            return couponRepository.save(updated);
        }
        return null;
    }

    public boolean receiveCoupon(Long memberId, Long couponId) {
        return issueCouponToMember(couponId, memberId);
    }

    private String generateCouponCode() {
        String prefix = "COUPON";
        String suffix = String.format("%08d", new Random().nextInt(100000000));
        return prefix + suffix;
    }
}