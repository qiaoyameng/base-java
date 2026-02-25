package com.washshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.washshop.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Select("SELECT * FROM orders WHERE order_status = #{status} AND deleted = 0")
    List<Order> selectByStatus(@Param("status") Integer status);

    @Select("SELECT * FROM orders WHERE user_id = #{userId} AND deleted = 0 ORDER BY create_time DESC")
    List<Order> selectByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM orders WHERE store_id = #{storeId} AND deleted = 0 ORDER BY create_time DESC")
    List<Order> selectByStoreId(@Param("storeId") Long storeId);

    @Select("SELECT * FROM orders WHERE order_status = 3 AND ready_time <= #{time} AND reminder_count = 0 AND deleted = 0")
    List<Order> selectReadyOrdersForReminder(@Param("time") LocalDateTime time);

    @Select("SELECT * FROM orders WHERE order_no = #{orderNo} AND deleted = 0")
    Order selectByOrderNo(@Param("orderNo") String orderNo);

    @Update("UPDATE orders SET order_status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Update("UPDATE orders SET reminder_count = reminder_count + 1 WHERE id = #{id}")
    int incrementReminderCount(@Param("id") Long id);
}
