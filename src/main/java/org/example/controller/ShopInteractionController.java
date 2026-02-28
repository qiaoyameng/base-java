package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.common.PageResult;
import org.example.common.Result;
import org.example.service.ShopInteractionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interaction")
@RequiredArgsConstructor
@Tag(name = "店铺互动", description = "体验分享、照片墙、优惠券、公告等互动功能接口")
public class ShopInteractionController {

    private final ShopInteractionService shopInteractionService;

    @PostMapping("/experiences")
    @Operation(summary = "发布体验分享", description = "顾客发布商品使用体验分享")
    public Result<ExperienceShareDTO> createExperienceShare(@Valid @RequestBody ExperienceShareDTO dto) {
        return Result.success(shopInteractionService.createExperienceShare(dto));
    }

    @GetMapping("/experiences")
    @Operation(summary = "查询体验分享列表", description = "分页查询所有体验分享")
    public Result<PageResult<ExperienceShareDTO>> listExperienceShares(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(shopInteractionService.listExperienceShares(pageNum, pageSize));
    }

    @GetMapping("/experiences/featured")
    @Operation(summary = "查询精选体验", description = "查询精选的体验分享")
    public Result<List<ExperienceShareDTO>> listFeaturedExperienceShares() {
        return Result.success(shopInteractionService.listFeaturedExperienceShares());
    }

    @PostMapping("/experiences/{id}/like")
    @Operation(summary = "点赞体验分享", description = "为体验分享点赞")
    public Result<Void> likeExperienceShare(
            @Parameter(description = "分享ID") @PathVariable Long id) {
        shopInteractionService.likeExperienceShare(id);
        return Result.success();
    }

    @PostMapping("/photos")
    @Operation(summary = "上传照片", description = "上传商品实拍照片到照片墙")
    public Result<PhotoWallDTO> uploadPhoto(@Valid @RequestBody PhotoWallDTO dto) {
        return Result.success(shopInteractionService.uploadPhoto(dto));
    }

    @GetMapping("/photos")
    @Operation(summary = "查询照片墙", description = "分页查询照片墙（只显示已审核）")
    public Result<PageResult<PhotoWallDTO>> listPhotos(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(shopInteractionService.listPhotos(pageNum, pageSize));
    }

    @PostMapping("/photos/{id}/like")
    @Operation(summary = "点赞照片", description = "为照片墙照片点赞")
    public Result<Void> likePhoto(
            @Parameter(description = "照片ID") @PathVariable Long id) {
        shopInteractionService.likePhoto(id);
        return Result.success();
    }

    @PostMapping("/photos/{id}/approve")
    @Operation(summary = "审核通过照片", description = "商家审核通过照片墙照片")
    public Result<Void> approvePhoto(
            @Parameter(description = "照片ID") @PathVariable Long id) {
        shopInteractionService.approvePhoto(id);
        return Result.success();
    }

    @PostMapping("/coupons")
    @Operation(summary = "创建优惠券", description = "商家创建优惠券活动")
    public Result<CouponDTO> createCoupon(@Valid @RequestBody CouponDTO dto) {
        return Result.success(shopInteractionService.createCoupon(dto));
    }

    @GetMapping("/coupons")
    @Operation(summary = "查询优惠券列表", description = "分页查询所有有效优惠券")
    public Result<PageResult<CouponDTO>> listActiveCoupons(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(shopInteractionService.listActiveCoupons(pageNum, pageSize));
    }

    @GetMapping("/coupons/home")
    @Operation(summary = "查询首页优惠券", description = "查询首页展示的优惠券")
    public Result<List<CouponDTO>> listHomeCoupons() {
        return Result.success(shopInteractionService.listHomeCoupons());
    }

    @PostMapping("/coupons/{couponId}/receive")
    @Operation(summary = "领取优惠券", description = "顾客领取优惠券")
    public Result<CustomerCouponDTO> receiveCoupon(
            @Parameter(description = "顾客ID") @RequestParam Long customerId,
            @Parameter(description = "优惠券ID") @PathVariable Long couponId) {
        return Result.success(shopInteractionService.receiveCoupon(customerId, couponId));
    }

    @GetMapping("/coupons/customer/{customerId}")
    @Operation(summary = "查询顾客优惠券", description = "分页查询顾客领取的所有优惠券")
    public Result<PageResult<CustomerCouponDTO>> listCustomerCoupons(
            @Parameter(description = "顾客ID") @PathVariable Long customerId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(shopInteractionService.listCustomerCoupons(customerId, pageNum, pageSize));
    }

    @GetMapping("/coupons/customer/{customerId}/valid")
    @Operation(summary = "查询顾客有效优惠券", description = "查询顾客未使用且未过期的优惠券")
    public Result<List<CustomerCouponDTO>> listCustomerValidCoupons(
            @Parameter(description = "顾客ID") @PathVariable Long customerId) {
        return Result.success(shopInteractionService.listCustomerValidCoupons(customerId));
    }

    @PostMapping("/coupons/use")
    @Operation(summary = "使用优惠券", description = "订单使用优惠券")
    public Result<Void> useCoupon(
            @Parameter(description = "顾客优惠券ID") @RequestParam Long customerCouponId,
            @Parameter(description = "订单ID") @RequestParam Long orderId) {
        shopInteractionService.useCoupon(customerCouponId, orderId);
        return Result.success();
    }

    @PostMapping("/announcements")
    @Operation(summary = "创建公告", description = "商家发布活动公告")
    public Result<AnnouncementDTO> createAnnouncement(@Valid @RequestBody AnnouncementDTO dto) {
        return Result.success(shopInteractionService.createAnnouncement(dto));
    }

    @GetMapping("/announcements")
    @Operation(summary = "查询公告列表", description = "分页查询所有有效公告")
    public Result<PageResult<AnnouncementDTO>> listAnnouncements(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(shopInteractionService.listAnnouncements(pageNum, pageSize));
    }

    @GetMapping("/announcements/home")
    @Operation(summary = "查询首页公告", description = "查询首页展示的公告")
    public Result<List<AnnouncementDTO>> listHomeAnnouncements() {
        return Result.success(shopInteractionService.listHomeAnnouncements());
    }

    @PostMapping("/announcements/{id}/view")
    @Operation(summary = "增加公告浏览量", description = "公告浏览量+1")
    public Result<Void> incrementAnnouncementView(
            @Parameter(description = "公告ID") @PathVariable Long id) {
        shopInteractionService.incrementAnnouncementView(id);
        return Result.success();
    }
}
