package org.example.order.controller;

import org.example.common.Result;
import org.example.order.entity.RefundRecord;
import org.example.order.service.RefundService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/refunds")
public class RefundController {
    
    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @GetMapping
    public Result<List<RefundRecord>> list() {
        return Result.success(refundService.findAll());
    }

    @GetMapping("/{id}")
    public Result<RefundRecord> getById(@PathVariable Long id) {
        return refundService.findById(id)
                .map(Result::success)
                .orElse(Result.error("退款记录不存在"));
    }

    @GetMapping("/no/{refundNo}")
    public Result<RefundRecord> getByRefundNo(@PathVariable String refundNo) {
        return refundService.findByRefundNo(refundNo)
                .map(Result::success)
                .orElse(Result.error("退款记录不存在"));
    }

    @GetMapping("/order/{orderId}")
    public Result<List<RefundRecord>> getByOrder(@PathVariable Long orderId) {
        return Result.success(refundService.findByOrderId(orderId));
    }

    @GetMapping("/member/{memberId}")
    public Result<List<RefundRecord>> getByMember(@PathVariable Long memberId) {
        return Result.success(refundService.findByMemberId(memberId));
    }

    @GetMapping("/pending")
    public Result<List<RefundRecord>> getPendingRefunds() {
        return Result.success(refundService.findPendingRefunds());
    }

    @PostMapping("/apply")
    public Result<RefundRecord> apply(
            @RequestParam Long orderId,
            @RequestParam Long memberId,
            @RequestParam BigDecimal amount,
            @RequestParam String reason) {
        return Result.success(refundService.applyRefund(orderId, memberId, amount, reason));
    }

    @PostMapping("/{id}/audit")
    public Result<Void> audit(
            @PathVariable Long id,
            @RequestParam Long auditorId,
            @RequestParam boolean approved,
            @RequestParam(required = false) String remark) {
        if (refundService.auditRefund(id, auditorId, approved, remark)) {
            return Result.success();
        }
        return Result.error("审核失败，退款记录不存在或已审核");
    }
}
