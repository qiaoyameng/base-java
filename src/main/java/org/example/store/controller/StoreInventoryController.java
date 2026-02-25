package org.example.store.controller;

import org.example.common.Result;
import org.example.store.entity.StoreInventory;
import org.example.store.service.StoreInventoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class StoreInventoryController {
    
    private final StoreInventoryService inventoryService;

    public StoreInventoryController(StoreInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/store/{storeId}")
    public Result<List<StoreInventory>> listByStore(@PathVariable Long storeId) {
        return Result.success(inventoryService.findByStoreId(storeId));
    }

    @GetMapping("/page")
    public Result<Page<StoreInventory>> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(inventoryService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public Result<StoreInventory> getById(@PathVariable Long id) {
        return inventoryService.findById(id)
                .map(Result::success)
                .orElse(Result.error("库存记录不存在"));
    }

    @PostMapping
    public Result<StoreInventory> create(@RequestBody StoreInventory inventory) {
        return Result.success(inventoryService.save(inventory));
    }

    @PutMapping("/{id}")
    public Result<StoreInventory> update(@PathVariable Long id, @RequestBody StoreInventory inventory) {
        StoreInventory updated = inventoryService.update(id, inventory);
        if (updated == null) {
            return Result.error("库存记录不存在");
        }
        return Result.success(updated);
    }

    @PostMapping("/{id}/adjust")
    public Result<Void> adjustQuantity(@PathVariable Long id, @RequestParam Integer quantity) {
        if (inventoryService.adjustQuantity(id, quantity)) {
            return Result.success();
        }
        return Result.error("库存记录不存在");
    }

    @GetMapping("/store/{storeId}/low-stock")
    public Result<List<StoreInventory>> getLowStock(@PathVariable Long storeId) {
        return Result.success(inventoryService.findLowStock(storeId));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (inventoryService.delete(id)) {
            return Result.success();
        }
        return Result.error("库存记录不存在");
    }
}
