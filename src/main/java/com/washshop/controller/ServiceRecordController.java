package com.washshop.controller;

import com.washshop.dto.ReviewDTO;
import com.washshop.service.ServiceRecordService;
import com.washshop.vo.Result;
import com.washshop.vo.ServiceRecordVO;
import com.washshop.vo.VoucherVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-record")
@Tag(name = "服务记录管理", description = "服务记录、评价、电子凭证相关接口")
public class ServiceRecordController {

    @Autowired
    private ServiceRecordService serviceRecordService;

    @PostMapping("/{recordId}/review")
    @Operation(summary = "提交评价")
    public Result<Void> submitReview(@RequestAttribute("userId") Long userId,
                                     @PathVariable Long recordId,
                                     @RequestBody @Validated ReviewDTO dto) {
        return serviceRecordService.submitReview(userId, recordId, dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取服务记录详情")
    public Result<ServiceRecordVO> getRecordById(@PathVariable Long id) {
        return serviceRecordService.getRecordById(id);
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "获取订单的服务记录")
    public Result<List<ServiceRecordVO>> getRecordsByOrderId(@PathVariable Long orderId) {
        return serviceRecordService.getRecordsByOrderId(orderId);
    }

    @GetMapping("/my-records")
    @Operation(summary = "获取我的服务记录")
    public Result<List<ServiceRecordVO>> getMyRecords(@RequestAttribute("userId") Long userId) {
        return serviceRecordService.getRecordsByUserId(userId);
    }

    @PostMapping("/order/{orderId}/voucher")
    @Operation(summary = "生成电子凭证")
    public Result<VoucherVO> generateVoucher(@PathVariable Long orderId) {
        return serviceRecordService.generateVoucher(orderId);
    }

    @GetMapping("/voucher/{voucherNo}")
    @Operation(summary = "查询电子凭证")
    public Result<VoucherVO> getVoucherByNo(@PathVariable String voucherNo) {
        return serviceRecordService.getVoucherByNo(voucherNo);
    }
}
