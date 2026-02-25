package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.dto.StoreRequest;
import org.example.dto.StoreResponse;
import org.example.service.StoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "门店管理")
@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @Operation(summary = "创建门店")
    @PostMapping
    public Result<StoreResponse> createStore(@Valid @RequestBody StoreRequest request) {
        return Result.success(storeService.createStore(request));
    }

    @Operation(summary = "获取门店详情")
    @GetMapping("/{id}")
    public Result<StoreResponse> getStore(@PathVariable Long id) {
        return Result.success(storeService.getStore(id));
    }

    @Operation(summary = "获取所有门店")
    @GetMapping
    public Result<List<StoreResponse>> getAllStores() {
        return Result.success(storeService.getAllStores());
    }

    @Operation(summary = "更新门店")
    @PutMapping("/{id}")
    public Result<StoreResponse> updateStore(@PathVariable Long id, @Valid @RequestBody StoreRequest request) {
        return Result.success(storeService.updateStore(id, request));
    }

    @Operation(summary = "删除门店")
    @DeleteMapping("/{id}")
    public Result<Void> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return Result.success();
    }
}
