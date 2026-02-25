package com.washshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.washshop.entity.ServiceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ServiceRecordMapper extends BaseMapper<ServiceRecord> {

    @Select("SELECT * FROM service_record WHERE order_id = #{orderId} AND deleted = 0")
    List<ServiceRecord> selectByOrderId(@Param("orderId") Long orderId);

    @Select("SELECT * FROM service_record WHERE user_id = #{userId} AND deleted = 0 ORDER BY create_time DESC")
    List<ServiceRecord> selectByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM service_record WHERE voucher_no = #{voucherNo} AND deleted = 0")
    ServiceRecord selectByVoucherNo(@Param("voucherNo") String voucherNo);
}
