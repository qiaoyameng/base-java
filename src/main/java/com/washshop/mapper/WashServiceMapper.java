package com.washshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.washshop.entity.WashService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WashServiceMapper extends BaseMapper<WashService> {

    @Select("SELECT * FROM wash_service WHERE category_id = #{categoryId} AND status = 1 AND deleted = 0 ORDER BY sort_order")
    List<WashService> selectByCategoryId(@Param("categoryId") Long categoryId);

    @Select("SELECT * FROM wash_service WHERE status = 1 AND deleted = 0 ORDER BY sort_order")
    List<WashService> selectActiveServices();
}
