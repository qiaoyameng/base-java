package com.washshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.washshop.entity.StoreInventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StoreInventoryMapper extends BaseMapper<StoreInventory> {

    @Select("SELECT * FROM store_inventory WHERE store_id = #{storeId} AND deleted = 0")
    List<StoreInventory> selectByStoreId(@Param("storeId") Long storeId);

    @Select("SELECT * FROM store_inventory WHERE store_id = #{storeId} AND item_type = #{itemType} AND deleted = 0")
    List<StoreInventory> selectByStoreIdAndType(@Param("storeId") Long storeId, @Param("itemType") Integer itemType);

    @Select("SELECT * FROM store_inventory WHERE store_id = #{storeId} AND stock <= min_stock AND deleted = 0")
    List<StoreInventory> selectLowStockItems(@Param("storeId") Long storeId);

    @Update("UPDATE store_inventory SET stock = stock + #{quantity} WHERE id = #{id}")
    int addStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    @Update("UPDATE store_inventory SET stock = stock - #{quantity} WHERE id = #{id} AND stock >= #{quantity}")
    int deductStock(@Param("id") Long id, @Param("quantity") Integer quantity);
}
