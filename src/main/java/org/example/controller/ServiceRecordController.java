package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.dto.ServiceRecordResponse;
import org.example.service.ServiceRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "服务记录管理")
@RestController
@RequestMapping("/api/service-records")
@RequiredArgsConstructor
public class ServiceRecordController {
    private final ServiceRecordService serviceRecordService;

    @Operation(summary = "获取服务记录详情")
    @GetMapping("/{id}")
    public Result<ServiceRecordResponse> getRecord(@PathVariable Long id) {
        return Result.success(serviceRecordService.getRecord(id));
    }

    @Operation(summary = "根据订单ID获取服务记录")
    @GetMapping("/order/{orderId}")
    public Result<ServiceRecordResponse> getRecordByOrderId(@PathVariable Long orderId) {
        return Result.success(serviceRecordService.getRecordByOrderId(orderId));
    }

    @Operation(summary = "根据订单号获取服务记录")
    @GetMapping("/order-no/{orderNo}")
    public Result<List<ServiceRecordResponse>> getRecordsByOrderNo(@PathVariable String orderNo) {
        return Result.success(serviceRecordService.getRecordsByOrderNo(orderNo));
    }

    @Operation(summary = "我的服务记录")
    @GetMapping("/customer/{customerId}")
    public Result<List<ServiceRecordResponse>> getMyRecords(@PathVariable Long customerId) {
        return Result.success(serviceRecordService.getRecordsByCustomer(customerId));
    }
}
