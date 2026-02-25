package com.washshop.controller;

import com.washshop.dto.CouponDTO;
import com.washshop.service.CouponService;
import com.washshop.vo.CouponVO;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;
import com.washshop.vo.UserCouponVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/coupon")
@Tag(name = "优惠券管理", description = "优惠券相关接口")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping
    @Operation(summary = "创建优惠券")
    public Result<Void> createCoupon(@RequestBody @Validated CouponDTO dto) {
        return couponService.createCoupon(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新优惠券")
    public Result<Void> updateCoupon(@PathVariable Long id, @RequestBody @Validated CouponDTO dto) {
        return couponService.updateCoupon(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除优惠券")
    public Result<Void> deleteCoupon(@PathVariable Long id) {
        return couponService.deleteCoupon(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取优惠券详情")
    public Result<CouponVO> getCouponById(@PathVariable Long id) {
        return couponService.getCouponById(id);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询优惠券")
    public Result<PageVO<CouponVO>> getCouponPage(@RequestParam(required = false) Integer status,
                                                  @RequestParam(defaultValue = "1") Long current,
                                                  @RequestParam(defaultValue = "10") Long size) {
        return couponService.getCouponPage(status, current, size);
    }

    @GetMapping("/active")
    @Operation(summary = "获取可用优惠券列表")
    public Result<List<CouponVO>> getActiveCoupons(@RequestAttribute(value = "userId", required = false) Long userId) {
        return couponService.getActiveCoupons(userId);
    }

    @PostMapping("/{couponId}/receive")
    @Operation(summary = "领取优惠券")
    public Result<Void> receiveCoupon(@RequestAttribute("userId") Long userId, @PathVariable Long couponId) {
        return couponService.receiveCoupon(userId, couponId);
    }

    @GetMapping("/my-coupons")
    @Operation(summary = "获取我的优惠券")
    public Result<List<UserCouponVO>> getUserCoupons(@RequestAttribute("userId") Long userId,
                                                     @RequestParam(required = false) Integer status) {
        return couponService.getUserCoupons(userId, status);
    }

    @GetMapping("/calculate-discount")
    @Operation(summary = "计算优惠金额")
    public Result<BigDecimal> calculateDiscount(@RequestParam Long userCouponId,
                                                @RequestParam BigDecimal orderAmount) {
        return couponService.calculateDiscount(userCouponId, orderAmount);
    }
}
