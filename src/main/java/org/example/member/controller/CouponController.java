package org.example.member.controller;

import org.example.common.Result;
import org.example.common.enums.CouponType;
import org.example.member.entity.Coupon;
import org.example.member.entity.MemberCoupon;
import org.example.member.service.CouponService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    public Result<List<Coupon>> list() {
        return Result.success(couponService.findAll());
    }

    @GetMapping("/page")
    public Result<Page<Coupon>> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(couponService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public Result<Coupon> getById(@PathVariable Long id) {
        return couponService.findById(id)
                .map(Result::success)
                .orElse(Result.error("优惠券不存在"));
    }

    @GetMapping("/code/{code}")
    public Result<Coupon> getByCode(@PathVariable String code) {
        return couponService.findByCode(code)
                .map(Result::success)
                .orElse(Result.error("优惠券不存在"));
    }

    @GetMapping("/type/{type}")
    public Result<List<Coupon>> getByType(@PathVariable CouponType type) {
        return Result.success(couponService.findByType(type));
    }

    @PostMapping
    public Result<Coupon> create(@RequestBody Coupon coupon) {
        return Result.success(couponService.create(coupon));
    }

    @PutMapping("/{id}")
    public Result<Coupon> update(@PathVariable Long id, @RequestBody Coupon coupon) {
        Coupon updated = couponService.update(id, coupon);
        if (updated == null) {
            return Result.error("优惠券不存在");
        }
        return Result.success(updated);
    }

    @PostMapping("/{id}/receive")
    public Result<Void> receive(@PathVariable Long id, @RequestParam Long memberId) {
        if (couponService.receiveCoupon(memberId, id)) {
            return Result.success();
        }
        return Result.error("领取失败，优惠券已领完或不存在");
    }

    @GetMapping("/member/{memberId}")
    public Result<List<MemberCoupon>> getMemberCoupons(@PathVariable Long memberId) {
        return Result.success(couponService.getMemberCoupons(memberId));
    }

    @GetMapping("/member/{memberId}/available")
    public Result<List<MemberCoupon>> getAvailableCoupons(@PathVariable Long memberId) {
        return Result.success(couponService.getAvailableCoupons(memberId));
    }

    @PostMapping("/member-coupon/{memberCouponId}/use")
    public Result<Void> useCoupon(
            @PathVariable Long memberCouponId,
            @RequestParam Long memberId,
            @RequestParam Long orderId) {
        if (couponService.useCoupon(memberCouponId, memberId, orderId)) {
            return Result.success();
        }
        return Result.error("使用失败，优惠券不可用或已过期");
    }
}
