package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.dto.InventoryRequest;
import org.example.entity.Inventory;
import org.example.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "库存管理")
@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @Operation(summary = "创建库存")
    @PostMapping
    public Result<Inventory> createInventory(@Valid @RequestBody InventoryRequest request) {
        return Result.success(inventoryService.createInventory(request));
    }

    @Operation(summary = "获取门店库存列表")
    @GetMapping("/store/{storeId}")
    public Result<List<Inventory>> getByStore(@PathVariable Long storeId) {
        return Result.success(inventoryService.getByStore(storeId));
    }

    @Operation(summary = "更新库存")
    @PutMapping("/{id}")
    public Result<Inventory> updateInventory(@PathVariable Long id, @Valid @RequestBody InventoryRequest request) {
        return Result.success(inventoryService.updateInventory(id, request));
    }

    @Operation(summary = "库存调整")
    @PostMapping("/{id}/adjust")
    public Result<Void> adjustStock(
            @PathVariable Long id,
            @RequestParam Integer amount,
            @RequestParam(required = false) String reason) {
        inventoryService.adjustStock(id, amount, reason);
        return Result.success();
    }

    @Operation(summary = "删除库存")
    @DeleteMapping("/{id}")
    public Result<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return Result.success();
    }
}
