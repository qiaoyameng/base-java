package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.dto.CouponRequest;
import org.example.dto.CouponResponse;
import org.example.dto.StoredCardRequest;
import org.example.dto.StoredCardResponse;
import org.example.service.CouponService;
import org.example.service.StoredCardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "优惠券管理")
@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @Operation(summary = "创建优惠券")
    @PostMapping
    public Result<CouponResponse> createCoupon(@Valid @RequestBody CouponRequest request) {
        return Result.success(couponService.createCoupon(request));
    }

    @Operation(summary = "发放优惠券给客户")
    @PostMapping("/{couponId}/distribute")
    public Result<Void> distributeCoupon(@PathVariable Long couponId, @RequestParam Long customerId) {
        couponService.distributeCoupon(couponId, customerId);
        return Result.success();
    }

    @Operation(summary = "获取我的可用优惠券")
    @GetMapping("/my")
    public Result<List<CouponResponse>> getMyCoupons() {
        return Result.success(couponService.getAvailableCoupons(1L));
    }

    @Operation(summary = "获取所有优惠券")
    @GetMapping
    public Result<Page<CouponResponse>> getAllCoupons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return Result.success(couponService.getAllCoupons(pageable));
    }

    @Operation(summary = "更新优惠券")
    @PutMapping("/{id}")
    public Result<CouponResponse> updateCoupon(@PathVariable Long id, @Valid @RequestBody CouponRequest request) {
        return Result.success(couponService.updateCoupon(id, request));
    }

    @Operation(summary = "删除优惠券")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return Result.success();
    }
}
