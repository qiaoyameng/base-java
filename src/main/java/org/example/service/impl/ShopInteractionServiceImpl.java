package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.entity.*;
import org.example.common.PageResult;
import org.example.repository.*;
import org.example.service.ShopInteractionService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopInteractionServiceImpl implements ShopInteractionService {

    private final ExperienceShareRepository experienceShareRepository;
    private final PhotoWallRepository photoWallRepository;
    private final CouponRepository couponRepository;
    private final CustomerCouponRepository customerCouponRepository;
    private final AnnouncementRepository announcementRepository;

    @Override
    @Transactional
    public ExperienceShareDTO createExperienceShare(ExperienceShareDTO dto) {
        ExperienceShare share = new ExperienceShare();
        BeanUtils.copyProperties(dto, share);
        share.setLikeCount(0);
        share.setCommentCount(0);
        share.setFeatured(false);
        share.setDeleted(false);
        ExperienceShare saved = experienceShareRepository.save(share);
        return convertToExperienceShareDTO(saved);
    }

    @Override
    public PageResult<ExperienceShareDTO> listExperienceShares(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ExperienceShare> page = experienceShareRepository.findByDeletedFalseOrderByCreateTimeDesc(pageable);
        List<ExperienceShareDTO> dtoList = page.getContent().stream()
                .map(this::convertToExperienceShareDTO)
                .collect(Collectors.toList());
        Page<ExperienceShareDTO> dtoPage = new org.springframework.data.domain.PageImpl<>(dtoList, pageable, page.getTotalElements());
        return PageResult.of(dtoPage);
    }

    @Override
    public List<ExperienceShareDTO> listFeaturedExperienceShares() {
        return experienceShareRepository.findByFeaturedTrueAndDeletedFalseOrderBySortOrderAsc().stream()
                .map(this::convertToExperienceShareDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void likeExperienceShare(Long id) {
        experienceShareRepository.incrementLikeCount(id);
    }

    @Override
    @Transactional
    public PhotoWallDTO uploadPhoto(PhotoWallDTO dto) {
        PhotoWall photo = new PhotoWall();
        BeanUtils.copyProperties(dto, photo);
        photo.setLikeCount(0);
        photo.setApproved(false);
        photo.setDeleted(false);
        PhotoWall saved = photoWallRepository.save(photo);
        return convertToPhotoWallDTO(saved);
    }

    @Override
    public PageResult<PhotoWallDTO> listPhotos(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<PhotoWall> page = photoWallRepository.findByApprovedTrueAndDeletedFalse(pageable);
        List<PhotoWallDTO> dtoList = page.getContent().stream()
                .map(this::convertToPhotoWallDTO)
                .collect(Collectors.toList());
        Page<PhotoWallDTO> dtoPage = new org.springframework.data.domain.PageImpl<>(dtoList, pageable, page.getTotalElements());
        return PageResult.of(dtoPage);
    }

    @Override
    @Transactional
    public void likePhoto(Long id) {
        photoWallRepository.incrementLikeCount(id);
    }

    @Override
    @Transactional
    public void approvePhoto(Long id) {
        photoWallRepository.approvePhoto(id);
    }

    @Override
    @Transactional
    public CouponDTO createCoupon(CouponDTO dto) {
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(dto, coupon);
        coupon.setRemainingQuantity(dto.getTotalQuantity());
        Coupon saved = couponRepository.save(coupon);
        return convertToCouponDTO(saved);
    }

    @Override
    public PageResult<CouponDTO> listActiveCoupons(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        LocalDateTime now = LocalDateTime.now();
        Page<Coupon> page = couponRepository.findByActiveTrueAndStartTimeBeforeAndEndTimeAfterOrderBySortOrderAsc(now, now, pageable);
        List<CouponDTO> dtoList = page.getContent().stream()
                .map(this::convertToCouponDTO)
                .collect(Collectors.toList());
        Page<CouponDTO> dtoPage = new org.springframework.data.domain.PageImpl<>(dtoList, pageable, page.getTotalElements());
        return PageResult.of(dtoPage);
    }

    @Override
    public List<CouponDTO> listHomeCoupons() {
        LocalDateTime now = LocalDateTime.now();
        return couponRepository.findByActiveTrueAndDisplayOnHomeTrueAndStartTimeBeforeAndEndTimeAfterOrderBySortOrderAsc(now, now).stream()
                .map(this::convertToCouponDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerCouponDTO receiveCoupon(Long customerId, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("优惠券不存在"));

        if (!coupon.getActive() || coupon.getRemainingQuantity() <= 0) {
            throw new RuntimeException("优惠券已领完或已下架");
        }

        if (coupon.getEndTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("优惠券已过期");
        }

        Long receivedCount = customerCouponRepository.countByCustomerIdAndCouponIdAndDeletedFalse(customerId, couponId);
        if (receivedCount >= coupon.getLimitPerUser()) {
            throw new RuntimeException("已达到领取上限");
        }

        int result = couponRepository.deductRemainingQuantity(couponId);
        if (result <= 0) {
            throw new RuntimeException("优惠券领取失败");
        }

        CustomerCoupon customerCoupon = new CustomerCoupon();
        customerCoupon.setCustomerId(customerId);
        customerCoupon.setCoupon(coupon);
        customerCoupon.setReceiveTime(LocalDateTime.now());
        customerCoupon.setStatus(CustomerCoupon.Status.UNUSED);
        customerCoupon.setDeleted(false);
        CustomerCoupon saved = customerCouponRepository.save(customerCoupon);
        return convertToCustomerCouponDTO(saved);
    }

    @Override
    public PageResult<CustomerCouponDTO> listCustomerCoupons(Long customerId, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<CustomerCoupon> page = customerCouponRepository.findByCustomerIdAndDeletedFalse(customerId, pageable);
        List<CustomerCouponDTO> dtoList = page.getContent().stream()
                .map(this::convertToCustomerCouponDTO)
                .collect(Collectors.toList());
        Page<CustomerCouponDTO> dtoPage = new org.springframework.data.domain.PageImpl<>(dtoList, pageable, page.getTotalElements());
        return PageResult.of(dtoPage);
    }

    @Override
    public List<CustomerCouponDTO> listCustomerValidCoupons(Long customerId) {
        return customerCouponRepository.findValidCouponsByCustomerId(customerId, LocalDateTime.now()).stream()
                .map(this::convertToCustomerCouponDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void useCoupon(Long customerCouponId, Long orderId) {
        CustomerCoupon customerCoupon = customerCouponRepository.findById(customerCouponId)
                .orElseThrow(() -> new RuntimeException("优惠券不存在"));

        if (customerCoupon.getStatus() != CustomerCoupon.Status.UNUSED) {
            throw new RuntimeException("优惠券状态不正确");
        }

        if (customerCoupon.getCoupon().getEndTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("优惠券已过期");
        }

        customerCoupon.setStatus(CustomerCoupon.Status.USED);
        customerCoupon.setUseTime(LocalDateTime.now());
        customerCoupon.setOrderId(orderId);
        customerCouponRepository.save(customerCoupon);
    }

    @Override
    @Transactional
    public AnnouncementDTO createAnnouncement(AnnouncementDTO dto) {
        Announcement announcement = new Announcement();
        BeanUtils.copyProperties(dto, announcement);
        announcement.setViewCount(0);
        Announcement saved = announcementRepository.save(announcement);
        return convertToAnnouncementDTO(saved);
    }

    @Override
    public PageResult<AnnouncementDTO> listAnnouncements(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        LocalDateTime now = LocalDateTime.now();
        Page<Announcement> page = announcementRepository.findByActiveTrueAndStartTimeBeforeAndEndTimeAfterOrderByCreateTimeDesc(now, now, pageable);
        List<AnnouncementDTO> dtoList = page.getContent().stream()
                .map(this::convertToAnnouncementDTO)
                .collect(Collectors.toList());
        Page<AnnouncementDTO> dtoPage = new org.springframework.data.domain.PageImpl<>(dtoList, pageable, page.getTotalElements());
        return PageResult.of(dtoPage);
    }

    @Override
    public List<AnnouncementDTO> listHomeAnnouncements() {
        LocalDateTime now = LocalDateTime.now();
        return announcementRepository.findByActiveTrueAndDisplayOnHomeTrueAndStartTimeBeforeAndEndTimeAfterOrderBySortOrderAsc(now, now).stream()
                .map(this::convertToAnnouncementDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void incrementAnnouncementView(Long id) {
        announcementRepository.incrementViewCount(id);
    }

    private ExperienceShareDTO convertToExperienceShareDTO(ExperienceShare share) {
        ExperienceShareDTO dto = new ExperienceShareDTO();
        BeanUtils.copyProperties(share, dto);
        return dto;
    }

    private PhotoWallDTO convertToPhotoWallDTO(PhotoWall photo) {
        PhotoWallDTO dto = new PhotoWallDTO();
        BeanUtils.copyProperties(photo, dto);
        return dto;
    }

    private CouponDTO convertToCouponDTO(Coupon coupon) {
        CouponDTO dto = new CouponDTO();
        BeanUtils.copyProperties(coupon, dto);
        return dto;
    }

    private CustomerCouponDTO convertToCustomerCouponDTO(CustomerCoupon customerCoupon) {
        CustomerCouponDTO dto = new CustomerCouponDTO();
        BeanUtils.copyProperties(customerCoupon, dto);
        dto.setCoupon(convertToCouponDTO(customerCoupon.getCoupon()));
        return dto;
    }

    private AnnouncementDTO convertToAnnouncementDTO(Announcement announcement) {
        AnnouncementDTO dto = new AnnouncementDTO();
        BeanUtils.copyProperties(announcement, dto);
        return dto;
    }
}
