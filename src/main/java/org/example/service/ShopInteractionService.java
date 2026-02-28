package org.example.service;

import org.example.dto.*;
import org.example.common.PageResult;

import java.util.List;

public interface ShopInteractionService {

    ExperienceShareDTO createExperienceShare(ExperienceShareDTO dto);

    PageResult<ExperienceShareDTO> listExperienceShares(Integer pageNum, Integer pageSize);

    List<ExperienceShareDTO> listFeaturedExperienceShares();

    void likeExperienceShare(Long id);

    PhotoWallDTO uploadPhoto(PhotoWallDTO dto);

    PageResult<PhotoWallDTO> listPhotos(Integer pageNum, Integer pageSize);

    void likePhoto(Long id);

    void approvePhoto(Long id);

    CouponDTO createCoupon(CouponDTO dto);

    PageResult<CouponDTO> listActiveCoupons(Integer pageNum, Integer pageSize);

    List<CouponDTO> listHomeCoupons();

    CustomerCouponDTO receiveCoupon(Long customerId, Long couponId);

    PageResult<CustomerCouponDTO> listCustomerCoupons(Long customerId, Integer pageNum, Integer pageSize);

    List<CustomerCouponDTO> listCustomerValidCoupons(Long customerId);

    void useCoupon(Long customerCouponId, Long orderId);

    AnnouncementDTO createAnnouncement(AnnouncementDTO dto);

    PageResult<AnnouncementDTO> listAnnouncements(Integer pageNum, Integer pageSize);

    List<AnnouncementDTO> listHomeAnnouncements();

    void incrementAnnouncementView(Long id);
}
