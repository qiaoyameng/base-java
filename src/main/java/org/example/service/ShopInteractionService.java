package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.AnnouncementDTO;
import org.example.dto.CouponDTO;
import org.example.dto.ExperienceShareDTO;
import org.example.dto.PhotoWallDTO;
import org.example.entity.*;
import org.example.enums.MemberLevel;
import org.example.exception.BusinessException;
import org.example.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShopInteractionService {
    private final ExperienceShareRepository experienceShareRepository;
    private final PhotoWallRepository photoWallRepository;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final AnnouncementRepository announcementRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ExperienceShare createExperienceShare(Long memberId, ExperienceShareDTO dto) {
        ExperienceShare share = new ExperienceShare();
        share.setMemberId(memberId);
        share.setProductId(dto.getProductId());
        share.setTitle(dto.getTitle());
        share.setContent(dto.getContent());
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            share.setImages(String.join(",", dto.getImages()));
        }
        return experienceShareRepository.save(share);
    }

    public Page<ExperienceShare> getExperienceShares(Pageable pageable) {
        return experienceShareRepository.findByDeletedFalseOrderByCreateTimeDesc(pageable);
    }

    public Page<ExperienceShare> getPopularExperienceShares(Pageable pageable) {
        return experienceShareRepository.findPopular(pageable);
    }

    public Page<ExperienceShare> getMemberExperienceShares(Long memberId, Pageable pageable) {
        return experienceShareRepository.findByMemberIdAndDeletedFalse(memberId, pageable);
    }

    public Page<ExperienceShare> getProductExperienceShares(Long productId, Pageable pageable) {
        return experienceShareRepository.findByProductIdAndDeletedFalse(productId, pageable);
    }

    public Page<ExperienceShare> searchExperienceShares(String keyword, Pageable pageable) {
        return experienceShareRepository.search(keyword, pageable);
    }

    @Transactional
    public void likeExperienceShare(Long id) {
        ExperienceShare share = experienceShareRepository.findById(id)
                .orElseThrow(() -> new BusinessException("分享不存在"));
        share.setLikes(share.getLikes() + 1);
        experienceShareRepository.save(share);
    }

    @Transactional
    public void deleteExperienceShare(Long id, Long memberId) {
        ExperienceShare share = experienceShareRepository.findById(id)
                .orElseThrow(() -> new BusinessException("分享不存在"));
        if (!share.getMemberId().equals(memberId)) {
            throw new BusinessException("无权删除该分享");
        }
        share.setDeleted(true);
        experienceShareRepository.save(share);
    }

    @Transactional
    public PhotoWall createPhoto(Long memberId, PhotoWallDTO dto) {
        PhotoWall photo = new PhotoWall();
        photo.setMemberId(memberId);
        photo.setProductId(dto.getProductId());
        photo.setImageUrl(dto.getImageUrl());
        photo.setDescription(dto.getDescription());
        return photoWallRepository.save(photo);
    }

    public Page<PhotoWall> getApprovedPhotos(Pageable pageable) {
        return photoWallRepository.findByDeletedFalseAndApprovedTrueOrderByCreateTimeDesc(pageable);
    }

    public Page<PhotoWall> getMemberPhotos(Long memberId, Pageable pageable) {
        return photoWallRepository.findByMemberIdAndDeletedFalse(memberId, pageable);
    }

    public Page<PhotoWall> getProductPhotos(Long productId, Pageable pageable) {
        return photoWallRepository.findByProductIdAndDeletedFalseAndApprovedTrue(productId, pageable);
    }

    public List<PhotoWall> getPopularPhotos(Pageable pageable) {
        return photoWallRepository.findPopularPhotos(pageable);
    }

    @Transactional
    public void approvePhoto(Long id) {
        PhotoWall photo = photoWallRepository.findById(id)
                .orElseThrow(() -> new BusinessException("照片不存在"));
        photo.setApproved(true);
        photoWallRepository.save(photo);
    }

    @Transactional
    public void likePhoto(Long id) {
        PhotoWall photo = photoWallRepository.findById(id)
                .orElseThrow(() -> new BusinessException("照片不存在"));
        photo.setLikes(photo.getLikes() + 1);
        photoWallRepository.save(photo);
    }

    @Transactional
    public void deletePhoto(Long id, Long memberId) {
        PhotoWall photo = photoWallRepository.findById(id)
                .orElseThrow(() -> new BusinessException("照片不存在"));
        if (!photo.getMemberId().equals(memberId)) {
            throw new BusinessException("无权删除该照片");
        }
        photo.setDeleted(true);
        photoWallRepository.save(photo);
    }

    @Transactional
    public Coupon createCoupon(CouponDTO dto) {
        if (dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new BusinessException("结束时间不能早于开始时间");
        }
        Coupon coupon = new Coupon();
        coupon.setName(dto.getName());
        coupon.setCode(generateCouponCode());
        coupon.setDiscountAmount(dto.getDiscountAmount());
        coupon.setMinOrderAmount(dto.getMinOrderAmount());
        coupon.setTotalQuantity(dto.getTotalQuantity());
        coupon.setStartTime(dto.getStartTime());
        coupon.setEndTime(dto.getEndTime());
        coupon.setMemberLevel(dto.getMemberLevel());
        return couponRepository.save(coupon);
    }

    private String generateCouponCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public Page<Coupon> getCoupons(Pageable pageable) {
        return couponRepository.findByActiveTrueOrderByCreateTimeDesc(pageable);
    }

    public List<Coupon> getAvailableCoupons() {
        return couponRepository.findAvailableCoupons(LocalDateTime.now());
    }

    public List<Coupon> getAvailableCouponsForMember(Long memberId) {
        Member member = memberRepository.findByIdAndDeletedFalse(memberId)
                .orElseThrow(() -> new BusinessException("会员不存在"));
        String level = member.getLevel().name();
        return couponRepository.findAvailableCouponsByMemberLevel(LocalDateTime.now(), level);
    }

    @Transactional
    public MemberCoupon claimCoupon(Long memberId, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException("优惠券不存在"));

        if (!coupon.getActive()) {
            throw new BusinessException("优惠券已下架");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
            throw new BusinessException("优惠券不在有效期内");
        }

        if (coupon.getUsedQuantity() >= coupon.getTotalQuantity()) {
            throw new BusinessException("优惠券已领完");
        }

        if (memberCouponRepository.existsByMemberIdAndCouponId(memberId, couponId)) {
            throw new BusinessException("已领取该优惠券");
        }

        if (coupon.getMemberLevel() != null) {
            Member member = memberRepository.findByIdAndDeletedFalse(memberId)
                    .orElseThrow(() -> new BusinessException("会员不存在"));
            if (!coupon.getMemberLevel().equals(member.getLevel().name())) {
                throw new BusinessException("会员等级不满足要求");
            }
        }

        coupon.setUsedQuantity(coupon.getUsedQuantity() + 1);
        couponRepository.save(coupon);

        MemberCoupon memberCoupon = new MemberCoupon();
        memberCoupon.setMemberId(memberId);
        memberCoupon.setCouponId(couponId);
        memberCoupon.setCouponCode(coupon.getCode());
        return memberCouponRepository.save(memberCoupon);
    }

    public Page<MemberCoupon> getMemberCoupons(Long memberId, Pageable pageable) {
        return memberCouponRepository.findByMemberIdOrderByCreateTimeDesc(memberId, pageable);
    }

    public List<MemberCoupon> getMemberUnusedCoupons(Long memberId) {
        return memberCouponRepository.findByMemberIdAndUsedFalse(memberId);
    }

    @Transactional
    public void useCoupon(Long memberId, Long couponId, Long orderId) {
        MemberCoupon memberCoupon = memberCouponRepository.findByMemberIdAndCouponIdAndUsedFalse(memberId, couponId)
                .orElseThrow(() -> new BusinessException("优惠券不存在或已使用"));

        memberCoupon.setUsed(true);
        memberCoupon.setOrderId(orderId);
        memberCoupon.setUsedTime(LocalDateTime.now());
        memberCouponRepository.save(memberCoupon);
    }

    @Transactional
    public void deactivateCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new BusinessException("优惠券不存在"));
        coupon.setActive(false);
        couponRepository.save(coupon);
    }

    @Transactional
    public Announcement createAnnouncement(AnnouncementDTO dto) {
        Announcement announcement = new Announcement();
        announcement.setTitle(dto.getTitle());
        announcement.setContent(dto.getContent());
        announcement.setImageUrl(dto.getImageUrl());
        announcement.setType(dto.getType());
        announcement.setTop(dto.getTop());
        return announcementRepository.save(announcement);
    }

    @Transactional
    public Announcement updateAnnouncement(Long id, AnnouncementDTO dto) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new BusinessException("公告不存在"));
        announcement.setTitle(dto.getTitle());
        announcement.setContent(dto.getContent());
        announcement.setImageUrl(dto.getImageUrl());
        announcement.setType(dto.getType());
        announcement.setTop(dto.getTop());
        return announcementRepository.save(announcement);
    }

    public Page<Announcement> getActiveAnnouncements(Pageable pageable) {
        return announcementRepository.findByDeletedFalseAndActiveTrueOrderByTopDescCreateTimeDesc(pageable);
    }

    public List<Announcement> getTopAnnouncements() {
        return announcementRepository.findByDeletedFalseAndActiveTrueAndTopTrueOrderByCreateTimeDesc();
    }

    public Page<Announcement> getAnnouncementsByType(String type, Pageable pageable) {
        return announcementRepository.findByDeletedFalseAndActiveTrueAndTypeOrderByTopDescCreateTimeDesc(type, pageable);
    }

    @Transactional
    public void deleteAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new BusinessException("公告不存在"));
        announcement.setDeleted(true);
        announcementRepository.save(announcement);
    }
}
