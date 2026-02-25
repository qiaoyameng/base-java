package org.example.record.controller;

import org.example.common.Result;
import org.example.record.entity.ServiceRecord;
import org.example.record.service.ServiceRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-records")
public class ServiceRecordController {
    
    private final ServiceRecordService recordService;

    public ServiceRecordController(ServiceRecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping
    public Result<List<ServiceRecord>> list() {
        return Result.success(recordService.findAll());
    }

    @GetMapping("/{id}")
    public Result<ServiceRecord> getById(@PathVariable Long id) {
        return recordService.findById(id)
                .map(Result::success)
                .orElse(Result.error("服务记录不存在"));
    }

    @GetMapping("/order/{orderId}")
    public Result<List<ServiceRecord>> getByOrder(@PathVariable Long orderId) {
        return Result.success(recordService.findByOrderId(orderId));
    }

    @GetMapping("/member/{memberId}")
    public Result<List<ServiceRecord>> getByMember(@PathVariable Long memberId) {
        return Result.success(recordService.findByMemberId(memberId));
    }

    @GetMapping("/store/{storeId}")
    public Result<List<ServiceRecord>> getByStore(@PathVariable Long storeId) {
        return Result.success(recordService.findByStoreId(storeId));
    }

    @GetMapping("/employee/{employeeId}")
    public Result<List<ServiceRecord>> getByEmployee(@PathVariable Long employeeId) {
        return Result.success(recordService.findByEmployeeId(employeeId));
    }

    @PostMapping("/receive")
    public Result<ServiceRecord> receiveClothes(
            @RequestParam Long orderItemId,
            @RequestParam Long orderId,
            @RequestParam Long memberId,
            @RequestParam Long storeId,
            @RequestParam String clothesName,
            @RequestParam(required = false) String clothesImage,
            @RequestParam(required = false) String beforeImages) {
        return Result.success(recordService.receiveClothes(
                orderItemId, orderId, memberId, storeId, clothesName, clothesImage, beforeImages));
    }

    @PostMapping("/{id}/start")
    public Result<Void> startService(@PathVariable Long id, @RequestParam Long employeeId) {
        if (recordService.startService(id, employeeId)) {
            return Result.success();
        }
        return Result.error("服务记录不存在");
    }

    @PostMapping("/{id}/end")
    public Result<Void> endService(@PathVariable Long id, @RequestParam(required = false) String afterImages) {
        if (recordService.endService(id, afterImages)) {
            return Result.success();
        }
        return Result.error("服务记录不存在");
    }

    @PostMapping("/{id}/deliver")
    public Result<Void> deliverClothes(@PathVariable Long id) {
        if (recordService.deliverClothes(id)) {
            return Result.success();
        }
        return Result.error("服务记录不存在");
    }

    @GetMapping("/{id}/duration")
    public Result<Integer> getServiceDuration(@PathVariable Long id) {
        return Result.success(recordService.calculateServiceDuration(id));
    }
}
