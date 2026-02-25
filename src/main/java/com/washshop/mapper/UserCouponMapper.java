package com.washshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.washshop.entity.UserCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserCouponMapper extends BaseMapper<UserCoupon> {

    @Select("SELECT * FROM user_coupon WHERE user_id = #{userId} AND status = #{status} AND deleted = 0")
    List<UserCoupon> selectByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);

    @Select("SELECT COUNT(*) FROM user_coupon WHERE user_id = #{userId} AND coupon_id = #{couponId} AND deleted = 0")
    int countByUserAndCoupon(@Param("userId") Long userId, @Param("couponId") Long couponId);

    @Select("SELECT * FROM user_coupon WHERE user_id = #{userId} AND coupon_id = #{couponId} AND status = 0 AND deleted = 0 LIMIT 1")
    UserCoupon selectAvailableByUserAndCoupon(@Param("userId") Long userId, @Param("couponId") Long couponId);
}
