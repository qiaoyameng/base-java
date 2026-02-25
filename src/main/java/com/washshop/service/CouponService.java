package com.washshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.washshop.dto.CouponDTO;
import com.washshop.entity.Coupon;
import com.washshop.vo.CouponVO;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;
import com.washshop.vo.UserCouponVO;

import java.math.BigDecimal;
import java.util.List;

public interface CouponService extends IService<Coupon> {

    Result<Void> createCoupon(CouponDTO dto);

    Result<Void> updateCoupon(Long id, CouponDTO dto);

    Result<Void> deleteCoupon(Long id);

    Result<CouponVO> getCouponById(Long id);

    Result<PageVO<CouponVO>> getCouponPage(Integer status, Long current, Long size);

    Result<List<CouponVO>> getActiveCoupons(Long userId);

    Result<Void> receiveCoupon(Long userId, Long couponId);

    Result<List<UserCouponVO>> getUserCoupons(Long userId, Integer status);

    Result<BigDecimal> calculateDiscount(Long userCouponId, BigDecimal orderAmount);
}
