package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.common.PageResult;
import org.example.common.Result;
import org.example.dto.*;
import org.example.entity.*;
import org.example.service.ShopInteractionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopInteractionController {
    private final ShopInteractionService shopInteractionService;

    @PostMapping("/experiences")
    public Result<ExperienceShare> createExperienceShare(
            @RequestParam Long memberId,
            @Valid @RequestBody ExperienceShareDTO dto) {
        return Result.success(shopInteractionService.createExperienceShare(memberId, dto));
    }

    @GetMapping("/experiences")
    public Result<PageResult<ExperienceShare>> getExperienceShares(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<ExperienceShare> shares = shopInteractionService.getExperienceShares(pageRequest);
        return Result.success(PageResult.of(shares));
    }

    @GetMapping("/experiences/popular")
    public Result<PageResult<ExperienceShare>> getPopularExperienceShares(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ExperienceShare> shares = shopInteractionService.getPopularExperienceShares(pageRequest);
        return Result.success(PageResult.of(shares));
    }

    @GetMapping("/experiences/member/{memberId}")
    public Result<PageResult<ExperienceShare>> getMemberExperienceShares(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<ExperienceShare> shares = shopInteractionService.getMemberExperienceShares(memberId, pageRequest);
        return Result.success(PageResult.of(shares));
    }

    @GetMapping("/experiences/product/{productId}")
    public Result<PageResult<ExperienceShare>> getProductExperienceShares(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<ExperienceShare> shares = shopInteractionService.getProductExperienceShares(productId, pageRequest);
        return Result.success(PageResult.of(shares));
    }

    @GetMapping("/experiences/search")
    public Result<PageResult<ExperienceShare>> searchExperienceShares(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<ExperienceShare> shares = shopInteractionService.searchExperienceShares(keyword, pageRequest);
        return Result.success(PageResult.of(shares));
    }

    @PostMapping("/experiences/{id}/like")
    public Result<Void> likeExperienceShare(@PathVariable Long id) {
        shopInteractionService.likeExperienceShare(id);
        return Result.success();
    }

    @DeleteMapping("/experiences/{id}")
    public Result<Void> deleteExperienceShare(@PathVariable Long id, @RequestParam Long memberId) {
        shopInteractionService.deleteExperienceShare(id, memberId);
        return Result.success();
    }

    @PostMapping("/photos")
    public Result<PhotoWall> createPhoto(
            @RequestParam Long memberId,
            @Valid @RequestBody PhotoWallDTO dto) {
        return Result.success(shopInteractionService.createPhoto(memberId, dto));
    }

    @GetMapping("/photos")
    public Result<PageResult<PhotoWall>> getApprovedPhotos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<PhotoWall> photos = shopInteractionService.getApprovedPhotos(pageRequest);
        return Result.success(PageResult.of(photos));
    }

    @GetMapping("/photos/member/{memberId}")
    public Result<PageResult<PhotoWall>> getMemberPhotos(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<PhotoWall> photos = shopInteractionService.getMemberPhotos(memberId, pageRequest);
        return Result.success(PageResult.of(photos));
    }

    @GetMapping("/photos/product/{productId}")
    public Result<PageResult<PhotoWall>> getProductPhotos(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<PhotoWall> photos = shopInteractionService.getProductPhotos(productId, pageRequest);
        return Result.success(PageResult.of(photos));
    }

    @GetMapping("/photos/popular")
    public Result<List<PhotoWall>> getPopularPhotos(
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        return Result.success(shopInteractionService.getPopularPhotos(pageRequest));
    }

    @PostMapping("/photos/{id}/approve")
    public Result<Void> approvePhoto(@PathVariable Long id) {
        shopInteractionService.approvePhoto(id);
        return Result.success();
    }

    @PostMapping("/photos/{id}/like")
    public Result<Void> likePhoto(@PathVariable Long id) {
        shopInteractionService.likePhoto(id);
        return Result.success();
    }

    @DeleteMapping("/photos/{id}")
    public Result<Void> deletePhoto(@PathVariable Long id, @RequestParam Long memberId) {
        shopInteractionService.deletePhoto(id, memberId);
        return Result.success();
    }

    @PostMapping("/coupons")
    public Result<Coupon> createCoupon(@Valid @RequestBody CouponDTO dto) {
        return Result.success(shopInteractionService.createCoupon(dto));
    }

    @GetMapping("/coupons")
    public Result<PageResult<Coupon>> getCoupons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<Coupon> coupons = shopInteractionService.getCoupons(pageRequest);
        return Result.success(PageResult.of(coupons));
    }

    @GetMapping("/coupons/available")
    public Result<List<Coupon>> getAvailableCoupons() {
        return Result.success(shopInteractionService.getAvailableCoupons());
    }

    @GetMapping("/coupons/available/{memberId}")
    public Result<List<Coupon>> getAvailableCouponsForMember(@PathVariable Long memberId) {
        return Result.success(shopInteractionService.getAvailableCouponsForMember(memberId));
    }

    @PostMapping("/coupons/{couponId}/claim")
    public Result<MemberCoupon> claimCoupon(
            @PathVariable Long couponId,
            @RequestParam Long memberId) {
        return Result.success(shopInteractionService.claimCoupon(memberId, couponId));
    }

    @GetMapping("/coupons/member/{memberId}")
    public Result<PageResult<MemberCoupon>> getMemberCoupons(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<MemberCoupon> coupons = shopInteractionService.getMemberCoupons(memberId, pageRequest);
        return Result.success(PageResult.of(coupons));
    }

    @GetMapping("/coupons/member/{memberId}/unused")
    public Result<List<MemberCoupon>> getMemberUnusedCoupons(@PathVariable Long memberId) {
        return Result.success(shopInteractionService.getMemberUnusedCoupons(memberId));
    }

    @PostMapping("/coupons/use")
    public Result<Void> useCoupon(
            @RequestParam Long memberId,
            @RequestParam Long couponId,
            @RequestParam Long orderId) {
        shopInteractionService.useCoupon(memberId, couponId, orderId);
        return Result.success();
    }

    @PostMapping("/coupons/{id}/deactivate")
    public Result<Void> deactivateCoupon(@PathVariable Long id) {
        shopInteractionService.deactivateCoupon(id);
        return Result.success();
    }

    @PostMapping("/announcements")
    public Result<Announcement> createAnnouncement(@Valid @RequestBody AnnouncementDTO dto) {
        return Result.success(shopInteractionService.createAnnouncement(dto));
    }

    @PutMapping("/announcements/{id}")
    public Result<Announcement> updateAnnouncement(@PathVariable Long id, @Valid @RequestBody AnnouncementDTO dto) {
        return Result.success(shopInteractionService.updateAnnouncement(id, dto));
    }

    @GetMapping("/announcements")
    public Result<PageResult<Announcement>> getActiveAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<Announcement> announcements = shopInteractionService.getActiveAnnouncements(pageRequest);
        return Result.success(PageResult.of(announcements));
    }

    @GetMapping("/announcements/top")
    public Result<List<Announcement>> getTopAnnouncements() {
        return Result.success(shopInteractionService.getTopAnnouncements());
    }

    @GetMapping("/announcements/type/{type}")
    public Result<PageResult<Announcement>> getAnnouncementsByType(
            @PathVariable String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<Announcement> announcements = shopInteractionService.getAnnouncementsByType(type, pageRequest);
        return Result.success(PageResult.of(announcements));
    }

    @DeleteMapping("/announcements/{id}")
    public Result<Void> deleteAnnouncement(@PathVariable Long id) {
        shopInteractionService.deleteAnnouncement(id);
        return Result.success();
    }
}
