package org.example.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.Coupon;
import org.example.entity.CustomerCoupon;
import org.example.service.CouponService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public Result<Coupon> create(@RequestBody Coupon coupon) {
        return Result.success(couponService.createCoupon(coupon));
    }

    @GetMapping
    public Result<Page<Coupon>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(couponService.findAll(page, size));
    }

    @GetMapping("/active")
    public Result<Page<Coupon>> findActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(couponService.findActive(page, size));
    }

    @GetMapping("/available/{customerId}")
    public Result<List<Coupon>> findAvailableCoupons(@PathVariable Long customerId) {
        return Result.success(couponService.findAvailableCouponsForCustomer(customerId));
    }

    @GetMapping("/{id}")
    public Result<Coupon> findById(@PathVariable Long id) {
        return Result.success(couponService.findById(id));
    }

    @GetMapping("/code/{code}")
    public Result<Coupon> findByCode(@PathVariable String code) {
        return Result.success(couponService.findByCode(code));
    }

    @PostMapping("/claim")
    public Result<CustomerCoupon> claim(@RequestBody ClaimRequest request) {
        return Result.success(couponService.claimCoupon(request.getCustomerId(), request.getCouponId()));
    }

    @GetMapping("/my/{customerId}")
    public Result<List<CustomerCoupon>> findMyCoupons(
            @PathVariable Long customerId,
            @RequestParam(required = false) CustomerCoupon.CustomerCouponStatus status) {
        return Result.success(couponService.findMyCoupons(customerId, status));
    }

    @PostMapping("/calculate")
    public Result<BigDecimal> calculateDiscount(@RequestBody ApplyRequest request) {
        return Result.success(couponService.applyCoupon(request.getCustomerCouponId(), request.getOrderAmount()));
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Coupon.CouponStatus status) {
        couponService.updateCouponStatus(id, status);
        return Result.success(null);
    }

    @PutMapping("/{id}/enabled")
    public Result<Void> toggleEnabled(@PathVariable Long id, @RequestParam Boolean enabled) {
        couponService.toggleEnabled(id, enabled);
        return Result.success(null);
    }

    @Data
    public static class ClaimRequest {
        private Long customerId;
        private Long couponId;
    }

    @Data
    public static class ApplyRequest {
        private Long customerCouponId;
        private BigDecimal orderAmount;
    }
}
