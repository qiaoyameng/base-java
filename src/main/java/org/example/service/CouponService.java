package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.CouponRequest;
import org.example.dto.CouponResponse;
import org.example.entity.Coupon;
import org.example.entity.CouponUsage;
import org.example.entity.Customer;
import org.example.entity.Store;
import org.example.enums.CouponStatus;
import org.example.exception.BusinessException;
import org.example.mapstruct.CouponMapper;
import org.example.repository.CouponRepository;
import org.example.repository.CouponUsageRepository;
import org.example.repository.CustomerRepository;
import org.example.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponUsageRepository couponUsageRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final CouponMapper couponMapper;

    @Transactional
    public CouponResponse createCoupon(CouponRequest request) {
        Store store = null;
        if (request.getStoreId() != null) {
            store = storeRepository.findById(request.getStoreId())
                    .orElseThrow(() -> new BusinessException("门店不存在"));
        }

        Coupon coupon = couponMapper.toEntity(request);
        coupon.setStore(store);
        coupon.setUsedCount(0);
        coupon.setEnabled(true);

        Coupon saved = couponRepository.save(coupon);
        log.info("优惠券创建成功: couponId={}, name={}", saved.getId(), request.getName());
        return couponMapper.toResponse(saved);
    }

    @Transactional
    public void distributeCoupon(Long couponId, Long customerId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException("优惠券不存在"));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("客户不存在"));

        long count = couponUsageRepository.countByCouponIdAndCustomerId(couponId, customerId);
        if (coupon.getLimitPerUser() != null && count >= coupon.getLimitPerUser()) {
            throw new BusinessException("超出个人领取限制");
        }

        CouponUsage usage = new CouponUsage();
        usage.setCoupon(coupon);
        usage.setCustomer(customer);
        usage.setStatus(CouponStatus.ACTIVE);
        usage.setReceiveTime(LocalDateTime.now());
        couponUsageRepository.save(usage);
        log.info("优惠券发放成功: couponId={}, customerId={}", couponId, customerId);
    }

    public List<CouponResponse> getAvailableCoupons(Long customerId) {
        List<CouponUsage> usages = couponUsageRepository.findByCustomerIdAndStatus(customerId, CouponStatus.ACTIVE);
        LocalDateTime now = LocalDateTime.now();
        return usages.stream()
                .map(CouponUsage::getCoupon)
                .filter(c -> c.getStartTime().isBefore(now) && c.getEndTime().isAfter(now))
                .distinct()
                .map(couponMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Page<CouponResponse> getAllCoupons(Pageable pageable) {
        return couponRepository.findAll(pageable).map(couponMapper::toResponse);
    }

    @Transactional
    public CouponResponse updateCoupon(Long id, CouponRequest request) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new BusinessException("优惠券不存在"));

        coupon.setName(request.getName());
        coupon.setDescription(request.getDescription());
        coupon.setStartTime(request.getValidFrom());
        coupon.setEndTime(request.getValidTo());

        Coupon saved = couponRepository.save(coupon);
        return couponMapper.toResponse(saved);
    }

    @Transactional
    public void deleteCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new BusinessException("优惠券不存在"));
        coupon.setEnabled(false);
        couponRepository.save(coupon);
    }
}
