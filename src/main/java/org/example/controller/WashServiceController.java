package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.dto.WashServiceRequest;
import org.example.dto.WashServiceResponse;
import org.example.enums.ServiceCategory;
import org.example.enums.ServiceStatus;
import org.example.service.WashServiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Tag(name = "洗衣服务管理", description = "洗衣服务的增删改查接口")
public class WashServiceController {

    private final WashServiceService washServiceService;

    @PostMapping
    @Operation(summary = "创建洗衣服务")
    public Result<WashServiceResponse> createService(@RequestBody WashServiceRequest request) {
        return Result.success(washServiceService.createService(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新洗衣服务")
    public Result<WashServiceResponse> updateService(
            @PathVariable Long id,
            @RequestBody WashServiceRequest request) {
        return Result.success(washServiceService.updateService(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询服务详情")
    public Result<WashServiceResponse> getServiceById(@PathVariable Long id) {
        return Result.success(washServiceService.getServiceById(id));
    }

    @GetMapping
    @Operation(summary = "查询所有启用的服务")
    public Result<List<WashServiceResponse>> getAllEnabledServices() {
        return Result.success(washServiceService.getAllEnabledServices());
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "按类别查询服务")
    public Result<List<WashServiceResponse>> getServicesByCategory(@PathVariable ServiceCategory category) {
        return Result.success(washServiceService.getServicesByCategory(category));
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "按门店查询服务")
    public Result<List<WashServiceResponse>> getServicesByStore(@PathVariable Long storeId) {
        return Result.success(washServiceService.getServicesByStore(storeId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除服务（软删除）")
    public Result<Void> deleteService(@PathVariable Long id) {
        washServiceService.deleteService(id);
        return Result.success();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "更新服务状态")
    public Result<WashServiceResponse> updateServiceStatus(
            @PathVariable Long id,
            @RequestParam ServiceStatus status) {
        return Result.success(washServiceService.updateServiceStatus(id, status));
    }
}
