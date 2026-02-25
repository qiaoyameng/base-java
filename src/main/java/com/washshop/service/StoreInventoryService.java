package com.washshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.washshop.dto.StoreInventoryDTO;
import com.washshop.entity.StoreInventory;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;
import com.washshop.vo.StoreInventoryVO;

import java.util.List;

public interface StoreInventoryService extends IService<StoreInventory> {

    Result<Void> createInventory(StoreInventoryDTO dto);

    Result<Void> updateInventory(Long id, StoreInventoryDTO dto);

    Result<Void> deleteInventory(Long id);

    Result<StoreInventoryVO> getInventoryById(Long id);

    Result<PageVO<StoreInventoryVO>> getInventoryPage(Long storeId, Integer itemType, Long current, Long size);

    Result<List<StoreInventoryVO>> getInventoryByStore(Long storeId);

    Result<List<StoreInventoryVO>> getLowStockItems(Long storeId);

    Result<Void> addStock(Long id, Integer quantity);

    Result<Void> deductStock(Long id, Integer quantity);
}
