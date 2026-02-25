package com.washshop.controller;

import com.washshop.dto.StoreDTO;
import com.washshop.service.StoreService;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;
import com.washshop.vo.StoreVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
@Tag(name = "门店管理", description = "门店信息相关接口")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping
    @Operation(summary = "创建门店")
    public Result<Void> createStore(@RequestBody @Validated StoreDTO dto) {
        return storeService.createStore(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新门店")
    public Result<Void> updateStore(@PathVariable Long id, @RequestBody @Validated StoreDTO dto) {
        return storeService.updateStore(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除门店")
    public Result<Void> deleteStore(@PathVariable Long id) {
        return storeService.deleteStore(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取门店详情")
    public Result<StoreVO> getStoreById(@PathVariable Long id) {
        return storeService.getStoreById(id);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询门店")
    public Result<PageVO<StoreVO>> getStorePage(@RequestParam(required = false) Integer status,
                                                @RequestParam(defaultValue = "1") Long current,
                                                @RequestParam(defaultValue = "10") Long size) {
        return storeService.getStorePage(status, current, size);
    }

    @GetMapping("/active")
    @Operation(summary = "获取所有营业门店")
    public Result<List<StoreVO>> getActiveStores() {
        return storeService.getActiveStores();
    }
}
