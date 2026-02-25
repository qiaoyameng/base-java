package com.washshop.controller;

import com.washshop.dto.StoreInventoryDTO;
import com.washshop.service.StoreInventoryService;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;
import com.washshop.vo.StoreInventoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store-inventory")
@Tag(name = "门店库存管理", description = "门店库存相关接口")
public class StoreInventoryController {

    @Autowired
    private StoreInventoryService storeInventoryService;

    @PostMapping
    @Operation(summary = "创建库存记录")
    public Result<Void> createInventory(@RequestBody @Validated StoreInventoryDTO dto) {
        return storeInventoryService.createInventory(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新库存记录")
    public Result<Void> updateInventory(@PathVariable Long id, @RequestBody @Validated StoreInventoryDTO dto) {
        return storeInventoryService.updateInventory(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除库存记录")
    public Result<Void> deleteInventory(@PathVariable Long id) {
        return storeInventoryService.deleteInventory(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取库存详情")
    public Result<StoreInventoryVO> getInventoryById(@PathVariable Long id) {
        return storeInventoryService.getInventoryById(id);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询库存")
    public Result<PageVO<StoreInventoryVO>> getInventoryPage(@RequestParam(required = false) Long storeId,
                                                             @RequestParam(required = false) Integer itemType,
                                                             @RequestParam(defaultValue = "1") Long current,
                                                             @RequestParam(defaultValue = "10") Long size) {
        return storeInventoryService.getInventoryPage(storeId, itemType, current, size);
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "获取门店库存")
    public Result<List<StoreInventoryVO>> getInventoryByStore(@PathVariable Long storeId) {
        return storeInventoryService.getInventoryByStore(storeId);
    }

    @GetMapping("/low-stock/{storeId}")
    @Operation(summary = "获取低库存物品")
    public Result<List<StoreInventoryVO>> getLowStockItems(@PathVariable Long storeId) {
        return storeInventoryService.getLowStockItems(storeId);
    }

    @PostMapping("/{id}/add-stock")
    @Operation(summary = "增加库存")
    public Result<Void> addStock(@PathVariable Long id, @RequestParam Integer quantity) {
        return storeInventoryService.addStock(id, quantity);
    }

    @PostMapping("/{id}/deduct-stock")
    @Operation(summary = "减少库存")
    public Result<Void> deductStock(@PathVariable Long id, @RequestParam Integer quantity) {
        return storeInventoryService.deductStock(id, quantity);
    }
}
