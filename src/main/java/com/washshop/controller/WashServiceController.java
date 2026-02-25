package com.washshop.controller;

import com.washshop.dto.WashServiceDTO;
import com.washshop.entity.WashService;
import com.washshop.service.WashServiceService;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wash-service")
@Tag(name = "洗衣服务管理", description = "洗衣服务相关接口")
public class WashServiceController {

    @Autowired
    private WashServiceService washServiceService;

    @PostMapping
    @Operation(summary = "创建服务")
    public Result<Void> createService(@RequestBody @Validated WashServiceDTO dto) {
        return washServiceService.createService(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新服务")
    public Result<Void> updateService(@PathVariable Long id, @RequestBody @Validated WashServiceDTO dto) {
        return washServiceService.updateService(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除服务")
    public Result<Void> deleteService(@PathVariable Long id) {
        return washServiceService.deleteService(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取服务详情")
    public Result<WashService> getServiceById(@PathVariable Long id) {
        return washServiceService.getServiceById(id);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询服务")
    public Result<PageVO<WashService>> getServicePage(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        return washServiceService.getServicePage(categoryId, status, current, size);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "根据分类获取服务")
    public Result<List<WashService>> getServicesByCategory(@PathVariable Long categoryId) {
        return washServiceService.getServicesByCategory(categoryId);
    }

    @GetMapping("/active")
    @Operation(summary = "获取所有可用服务")
    public Result<List<WashService>> getActiveServices() {
        return washServiceService.getActiveServices();
    }
}
