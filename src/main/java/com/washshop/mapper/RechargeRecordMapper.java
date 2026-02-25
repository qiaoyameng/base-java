package com.washshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.washshop.entity.RechargeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RechargeRecordMapper extends BaseMapper<RechargeRecord> {

    @Select("SELECT * FROM recharge_record WHERE user_id = #{userId} AND deleted = 0 ORDER BY create_time DESC")
    List<RechargeRecord> selectByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM recharge_record WHERE recharge_no = #{rechargeNo} AND deleted = 0")
    RechargeRecord selectByRechargeNo(@Param("rechargeNo") String rechargeNo);
}
