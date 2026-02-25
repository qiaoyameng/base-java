package com.washshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.washshop.dto.CouponDTO;
import com.washshop.entity.Coupon;
import com.washshop.entity.UserCoupon;
import com.washshop.enums.CouponType;
import com.washshop.mapper.CouponMapper;
import com.washshop.mapper.UserCouponMapper;
import com.washshop.service.CouponService;
import com.washshop.vo.CouponVO;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;
import com.washshop.vo.UserCouponVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Override
    public Result<Void> createCoupon(CouponDTO dto) {
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(dto, coupon);
        coupon.setRemainingQuantity(dto.getTotalQuantity());
        save(coupon);
        return Result.success();
    }

    @Override
    public Result<Void> updateCoupon(Long id, CouponDTO dto) {
        Coupon coupon = getById(id);
        if (coupon == null) {
            return Result.error("优惠券不存在");
        }
        BeanUtils.copyProperties(dto, coupon);
        coupon.setId(id);
        updateById(coupon);
        return Result.success();
    }

    @Override
    public Result<Void> deleteCoupon(Long id) {
        removeById(id);
        return Result.success();
    }

    @Override
    public Result<CouponVO> getCouponById(Long id) {
        Coupon coupon = getById(id);
        if (coupon == null) {
            return Result.error("优惠券不存在");
        }
        return Result.success(convertToVO(coupon, false));
    }

    @Override
    public Result<PageVO<CouponVO>> getCouponPage(Integer status, Long current, Long size) {
        LambdaQueryWrapper<Coupon> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Coupon::getStatus, status);
        }
        wrapper.orderByDesc(Coupon::getCreateTime);

        Page<Coupon> page = new Page<>(current, size);
        page(page, wrapper);

        List<CouponVO> voList = page.getRecords().stream()
                .map(coupon -> convertToVO(coupon, false))
                .collect(Collectors.toList());
        PageVO<CouponVO> pageVO = new PageVO<>(page.getTotal(), current, size, voList);
        return Result.success(pageVO);
    }

    @Override
    public Result<List<CouponVO>> getActiveCoupons(Long userId) {
        List<Coupon> coupons = baseMapper.selectActiveCoupons(LocalDateTime.now());
        List<CouponVO> voList = coupons.stream()
                .map(coupon -> {
                    boolean received = false;
                    if (userId != null) {
                        int count = userCouponMapper.countByUserAndCoupon(userId, coupon.getId());
                        received = count > 0;
                    }
                    return convertToVO(coupon, received);
                })
                .collect(Collectors.toList());
        return Result.success(voList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> receiveCoupon(Long userId, Long couponId) {
        Coupon coupon = getById(couponId);
        if (coupon == null) {
            return Result.error("优惠券不存在");
        }
        if (coupon.getStatus() != 1) {
            return Result.error("优惠券已下架");
        }
        if (coupon.getStartTime().isAfter(LocalDateTime.now()) || coupon.getEndTime().isBefore(LocalDateTime.now())) {
            return Result.error("优惠券不在有效期内");
        }
        if (coupon.getRemainingQuantity() <= 0) {
            return Result.error("优惠券已领完");
        }

        if (coupon.getLimitPerUser() != null && coupon.getLimitPerUser() > 0) {
            int receivedCount = userCouponMapper.countByUserAndCoupon(userId, couponId);
            if (receivedCount >= coupon.getLimitPerUser()) {
                return Result.error("已达到领取上限");
            }
        }

        int rows = baseMapper.deductQuantity(couponId);
        if (rows == 0) {
            return Result.error("优惠券已领完");
        }

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setReceiveTime(LocalDateTime.now());
        userCoupon.setStatus(0);
        userCouponMapper.insert(userCoupon);

        return Result.success();
    }

    @Override
    public Result<List<UserCouponVO>> getUserCoupons(Long userId, Integer status) {
        List<UserCoupon> userCoupons = userCouponMapper.selectByUserIdAndStatus(userId, status);
        List<UserCouponVO> voList = userCoupons.stream().map(userCoupon -> {
            UserCouponVO vo = new UserCouponVO();
            BeanUtils.copyProperties(userCoupon, vo);

            Coupon coupon = getById(userCoupon.getCouponId());
            if (coupon != null) {
                vo.setCouponName(coupon.getCouponName());
                vo.setCouponType(coupon.getCouponType());
                vo.setDescription(coupon.getDescription());

                CouponType type = CouponType.fromCode(coupon.getCouponType());
                if (type != null) {
                    vo.setCouponTypeName(type.getDesc());
                }
            }

            String statusName;
            switch (userCoupon.getStatus()) {
                case 0:
                    statusName = "未使用";
                    break;
                case 1:
                    statusName = "已使用";
                    break;
                case 2:
                    statusName = "已过期";
                    break;
                default:
                    statusName = "未知";
            }
            vo.setStatusName(statusName);

            return vo;
        }).collect(Collectors.toList());
        return Result.success(voList);
    }

    @Override
    public Result<BigDecimal> calculateDiscount(Long userCouponId, BigDecimal orderAmount) {
        UserCoupon userCoupon = userCouponMapper.selectById(userCouponId);
        if (userCoupon == null || userCoupon.getStatus() != 0) {
            return Result.error("优惠券无效");
        }

        Coupon coupon = getById(userCoupon.getCouponId());
        if (coupon == null) {
            return Result.error("优惠券不存在");
        }

        if (orderAmount.compareTo(coupon.getMinAmount()) < 0) {
            return Result.error("订单金额未达到使用门槛");
        }

        BigDecimal discount = BigDecimal.ZERO;
        CouponType type = CouponType.fromCode(coupon.getCouponType());

        if (type == CouponType.FULL_REDUCTION) {
            discount = coupon.getDiscountAmount();
        } else if (type == CouponType.DISCOUNT) {
            discount = orderAmount.multiply(BigDecimal.ONE.subtract(coupon.getDiscountRate()));
            if (coupon.getMaxDiscount() != null && discount.compareTo(coupon.getMaxDiscount()) > 0) {
                discount = coupon.getMaxDiscount();
            }
        } else if (type == CouponType.FREE_SHIPPING) {
            discount = new BigDecimal("10.00");
        }

        return Result.success(discount);
    }

    private CouponVO convertToVO(Coupon coupon, boolean received) {
        CouponVO vo = new CouponVO();
        BeanUtils.copyProperties(coupon, vo);

        CouponType type = CouponType.fromCode(coupon.getCouponType());
        if (type != null) {
            vo.setCouponTypeName(type.getDesc());
        }

        vo.setReceived(received);
        return vo;
    }
}
