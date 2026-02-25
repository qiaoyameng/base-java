package com.washshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.washshop.entity.PointsRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PointsRecordMapper extends BaseMapper<PointsRecord> {

    @Select("SELECT * FROM points_record WHERE user_id = #{userId} AND deleted = 0 ORDER BY create_time DESC")
    List<PointsRecord> selectByUserId(@Param("userId") Long userId);
}
