package com.washshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.washshop.entity.Store;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StoreMapper extends BaseMapper<Store> {

    @Select("SELECT * FROM store WHERE status = 1 AND deleted = 0 ORDER BY sort_order")
    List<Store> selectActiveStores();

    @Select("SELECT * FROM store WHERE store_code = #{storeCode} AND deleted = 0")
    Store selectByStoreCode(@Param("storeCode") String storeCode);
}
