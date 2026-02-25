package org.example.record.controller;

import org.example.common.Result;
import org.example.record.entity.ServiceVoucher;
import org.example.record.service.ServiceVoucherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-vouchers")
public class ServiceVoucherController {
    
    private final ServiceVoucherService voucherService;

    public ServiceVoucherController(ServiceVoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping("/{id}")
    public Result<ServiceVoucher> getById(@PathVariable Long id) {
        return voucherService.findById(id)
                .map(Result::success)
                .orElse(Result.error("凭证不存在"));
    }

    @GetMapping("/no/{voucherNo}")
    public Result<ServiceVoucher> getByVoucherNo(@PathVariable String voucherNo) {
        return voucherService.findByVoucherNo(voucherNo)
                .map(Result::success)
                .orElse(Result.error("凭证不存在"));
    }

    @GetMapping("/order/{orderId}")
    public Result<List<ServiceVoucher>> getByOrder(@PathVariable Long orderId) {
        return Result.success(voucherService.findByOrderId(orderId));
    }

    @GetMapping("/member/{memberId}")
    public Result<List<ServiceVoucher>> getByMember(@PathVariable Long memberId) {
        return Result.success(voucherService.findByMemberId(memberId));
    }

    @PostMapping("/generate")
    public Result<ServiceVoucher> generateVoucher(@RequestParam Long orderId) {
        ServiceVoucher voucher = voucherService.generateVoucher(orderId);
        if (voucher == null) {
            return Result.error("订单不存在");
        }
        return Result.success(voucher);
    }

    @PostMapping("/generate-detail-sheet")
    public Result<ServiceVoucher> generateDetailSheet(@RequestParam Long orderId) {
        ServiceVoucher voucher = voucherService.generateServiceDetailSheet(orderId);
        if (voucher == null) {
            return Result.error("订单不存在");
        }
        return Result.success(voucher);
    }
}
