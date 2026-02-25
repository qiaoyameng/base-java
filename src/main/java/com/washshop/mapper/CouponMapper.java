package com.washshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.washshop.entity.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CouponMapper extends BaseMapper<Coupon> {

    @Select("SELECT * FROM coupon WHERE status = 1 AND start_time <= #{now} AND end_time >= #{now} AND remaining_quantity > 0 AND deleted = 0")
    List<Coupon> selectActiveCoupons(@Param("now") LocalDateTime now);

    @Update("UPDATE coupon SET remaining_quantity = remaining_quantity - 1 WHERE id = #{id} AND remaining_quantity > 0")
    int deductQuantity(@Param("id") Long id);
}
