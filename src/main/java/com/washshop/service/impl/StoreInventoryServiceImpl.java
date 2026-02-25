package com.washshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.washshop.dto.StoreInventoryDTO;
import com.washshop.entity.Store;
import com.washshop.entity.StoreInventory;
import com.washshop.mapper.StoreInventoryMapper;
import com.washshop.mapper.StoreMapper;
import com.washshop.service.StoreInventoryService;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;
import com.washshop.vo.StoreInventoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreInventoryServiceImpl extends ServiceImpl<StoreInventoryMapper, StoreInventory> implements StoreInventoryService {

    @Autowired
    private StoreMapper storeMapper;

    @Override
    public Result<Void> createInventory(StoreInventoryDTO dto) {
        StoreInventory inventory = new StoreInventory();
        BeanUtils.copyProperties(dto, inventory);
        save(inventory);
        return Result.success();
    }

    @Override
    public Result<Void> updateInventory(Long id, StoreInventoryDTO dto) {
        StoreInventory inventory = getById(id);
        if (inventory == null) {
            return Result.error("库存记录不存在");
        }
        BeanUtils.copyProperties(dto, inventory);
        inventory.setId(id);
        updateById(inventory);
        return Result.success();
    }

    @Override
    public Result<Void> deleteInventory(Long id) {
        removeById(id);
        return Result.success();
    }

    @Override
    public Result<StoreInventoryVO> getInventoryById(Long id) {
        StoreInventory inventory = getById(id);
        if (inventory == null) {
            return Result.error("库存记录不存在");
        }
        return Result.success(convertToVO(inventory));
    }

    @Override
    public Result<PageVO<StoreInventoryVO>> getInventoryPage(Long storeId, Integer itemType, Long current, Long size) {
        LambdaQueryWrapper<StoreInventory> wrapper = new LambdaQueryWrapper<>();
        if (storeId != null) {
            wrapper.eq(StoreInventory::getStoreId, storeId);
        }
        if (itemType != null) {
            wrapper.eq(StoreInventory::getItemType, itemType);
        }
        wrapper.orderByDesc(StoreInventory::getCreateTime);

        Page<StoreInventory> page = new Page<>(current, size);
        page(page, wrapper);

        List<StoreInventoryVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        PageVO<StoreInventoryVO> pageVO = new PageVO<>(page.getTotal(), current, size, voList);
        return Result.success(pageVO);
    }

    @Override
    public Result<List<StoreInventoryVO>> getInventoryByStore(Long storeId) {
        List<StoreInventory> list = baseMapper.selectByStoreId(storeId);
        List<StoreInventoryVO> voList = list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        return Result.success(voList);
    }

    @Override
    public Result<List<StoreInventoryVO>> getLowStockItems(Long storeId) {
        List<StoreInventory> list = baseMapper.selectLowStockItems(storeId);
        List<StoreInventoryVO> voList = list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        return Result.success(voList);
    }

    @Override
    public Result<Void> addStock(Long id, Integer quantity) {
        int rows = baseMapper.addStock(id, quantity);
        if (rows == 0) {
            return Result.error("添加库存失败");
        }
        return Result.success();
    }

    @Override
    public Result<Void> deductStock(Long id, Integer quantity) {
        int rows = baseMapper.deductStock(id, quantity);
        if (rows == 0) {
            return Result.error("库存不足");
        }
        return Result.success();
    }

    private StoreInventoryVO convertToVO(StoreInventory inventory) {
        StoreInventoryVO vo = new StoreInventoryVO();
        BeanUtils.copyProperties(inventory, vo);

        Store store = storeMapper.selectById(inventory.getStoreId());
        if (store != null) {
            vo.setStoreName(store.getStoreName());
        }

        String itemTypeName;
        switch (inventory.getItemType()) {
            case 1:
                itemTypeName = "洗护耗材";
                break;
            case 2:
                itemTypeName = "包装物料";
                break;
            default:
                itemTypeName = "其他";
        }
        vo.setItemTypeName(itemTypeName);

        vo.setLowStock(inventory.getStock() <= inventory.getMinStock());

        return vo;
    }
}
