package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Coupon;
import org.example.entity.Customer;
import org.example.entity.CustomerCoupon;
import org.example.repository.CouponRepository;
import org.example.repository.CustomerCouponRepository;
import org.example.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CustomerCouponRepository customerCouponRepository;
    private final CustomerRepository customerRepository;

    public Coupon createCoupon(Coupon coupon) {
        coupon.setUsedCount(0);
        coupon.setStatus(Coupon.CouponStatus.ACTIVE);
        coupon.setEnabled(true);
        return couponRepository.save(coupon);
    }

    public Page<Coupon> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return couponRepository.findAll(pageable);
    }

    public Page<Coupon> findActive(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return couponRepository.findByEnabled(true, pageable);
    }

    public List<Coupon> findAvailableCouponsForCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        List<String> levels = getLowerOrEqualLevels(customer.getMemberLevel());
        return couponRepository.findAvailableCoupons(LocalDateTime.now(), levels);
    }

    private List<String> getLowerOrEqualLevels(Customer.MemberLevel level) {
        return switch (level) {
            case GOLD -> List.of("NORMAL", "SILVER", "GOLD");
            case SILVER -> List.of("NORMAL", "SILVER");
            case NORMAL -> List.of("NORMAL");
        };
    }

    @Transactional
    public CustomerCoupon claimCoupon(Long customerId, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("优惠券不存在"));

        if (coupon.getUsedCount() >= coupon.getTotalCount()) {
            throw new RuntimeException("优惠券已领完");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getValidFrom()) || now.isAfter(coupon.getValidTo())) {
            throw new RuntimeException("不在优惠券有效期内");
        }

        long claimed = customerCouponRepository.countByCustomerIdAndCouponId(customerId, couponId);
        if (claimed >= coupon.getLimitPerCustomer()) {
            throw new RuntimeException("已达到领取上限");
        }

        CustomerCoupon customerCoupon = new CustomerCoupon();
        customerCoupon.setCustomerId(customerId);
        customerCoupon.setCouponId(couponId);
        customerCoupon.setCouponCode(coupon.getCouponCode());
        customerCoupon.setStatus(CustomerCoupon.CustomerCouponStatus.AVAILABLE);
        customerCoupon.setClaimedTime(LocalDateTime.now());
        customerCoupon.setValidFrom(coupon.getValidFrom());
        customerCoupon.setValidTo(coupon.getValidTo());

        customerCouponRepository.save(customerCoupon);

        coupon.setUsedCount(coupon.getUsedCount() + 1);
        if (coupon.getUsedCount() >= coupon.getTotalCount()) {
            coupon.setStatus(Coupon.CouponStatus.USED_UP);
        }
        couponRepository.save(coupon);

        return customerCoupon;
    }

    public List<CustomerCoupon> findMyCoupons(Long customerId, CustomerCoupon.CustomerCouponStatus status) {
        if (status != null) {
            return customerCouponRepository.findByCustomerIdAndStatus(customerId, status);
        }
        return customerCouponRepository.findByCustomerId(customerId);
    }

    public BigDecimal applyCoupon(Long customerCouponId, BigDecimal orderAmount) {
        CustomerCoupon customerCoupon = customerCouponRepository.findById(customerCouponId)
                .orElseThrow(() -> new RuntimeException("优惠券不存在"));

        if (customerCoupon.getStatus() != CustomerCoupon.CustomerCouponStatus.AVAILABLE) {
            throw new RuntimeException("优惠券状态无效");
        }

        Coupon coupon = couponRepository.findById(customerCoupon.getCouponId())
                .orElseThrow(() -> new RuntimeException("优惠券不存在"));

        if (orderAmount.compareTo(coupon.getMinAmount()) < 0) {
            throw new RuntimeException("订单金额不满足最低要求");
        }

        BigDecimal discount = BigDecimal.ZERO;
        switch (coupon.getType()) {
            case DISCOUNT -> {
                discount = orderAmount.multiply(coupon.getDiscountValue());
                if (coupon.getMaxDiscount() != null && discount.compareTo(coupon.getMaxDiscount()) > 0) {
                    discount = coupon.getMaxDiscount();
                }
            }
            case FIXED_AMOUNT -> {
                discount = coupon.getDiscountValue();
            }
            case GIFT -> {
                discount = BigDecimal.ZERO;
            }
        }

        if (discount.compareTo(orderAmount) > 0) {
            discount = orderAmount;
        }

        return discount;
    }

    @Transactional
    public void useCoupon(Long customerCouponId, Long orderId) {
        CustomerCoupon customerCoupon = customerCouponRepository.findById(customerCouponId)
                .orElseThrow(() -> new RuntimeException("优惠券不存在"));

        customerCoupon.setStatus(CustomerCoupon.CustomerCouponStatus.USED);
        customerCoupon.setUsedTime(LocalDateTime.now());
        customerCoupon.setOrderId(orderId);
        customerCouponRepository.save(customerCoupon);
    }

    @Transactional
    public void returnCoupon(Long customerCouponId) {
        CustomerCoupon customerCoupon = customerCouponRepository.findById(customerCouponId)
                .orElseThrow(() -> new RuntimeException("优惠券不存在"));

        if (LocalDateTime.now().isBefore(customerCoupon.getValidTo())) {
            customerCoupon.setStatus(CustomerCoupon.CustomerCouponStatus.AVAILABLE);
            customerCoupon.setUsedTime(null);
            customerCoupon.setOrderId(null);
            customerCouponRepository.save(customerCoupon);
        }
    }

    public Coupon findById(Long id) {
        return couponRepository.findById(id).orElse(null);
    }

    public Coupon findByCode(String code) {
        return couponRepository.findByCouponCode(code).orElse(null);
    }

    @Transactional
    public void updateCouponStatus(Long id, Coupon.CouponStatus status) {
        Coupon coupon = findById(id);
        if (coupon != null) {
            coupon.setStatus(status);
            couponRepository.save(coupon);
        }
    }

    @Transactional
    public void toggleEnabled(Long id, Boolean enabled) {
        Coupon coupon = findById(id);
        if (coupon != null) {
            coupon.setEnabled(enabled);
            couponRepository.save(coupon);
        }
    }

    @Transactional
    public void updateExpiredCoupons() {
        List<Coupon> expired = couponRepository.findExpiredCoupons(LocalDateTime.now());
        for (Coupon coupon : expired) {
            coupon.setStatus(Coupon.CouponStatus.EXPIRED);
        }
        couponRepository.saveAll(expired);

        List<CustomerCoupon> customerCoupons = customerCouponRepository.findByStatus(CustomerCoupon.CustomerCouponStatus.AVAILABLE);
        LocalDateTime now = LocalDateTime.now();
        for (CustomerCoupon cc : customerCoupons) {
            if (cc.getValidTo().isBefore(now)) {
                cc.setStatus(CustomerCoupon.CustomerCouponStatus.EXPIRED);
            }
        }
        customerCouponRepository.saveAll(customerCoupons);
    }
}
