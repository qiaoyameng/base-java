package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.TransactionRecord;
import org.example.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/{transactionNo}")
    public Result<TransactionRecord> findByTransactionNo(@PathVariable String transactionNo) {
        TransactionRecord record = transactionService.findByTransactionNo(transactionNo);
        if (record == null) {
            return Result.error("交易记录不存在");
        }
        return Result.success(record);
    }

    @GetMapping("/order/{orderNo}")
    public Result<TransactionRecord> findByOrderNo(@PathVariable String orderNo) {
        TransactionRecord record = transactionService.findByOrderNo(orderNo);
        if (record == null) {
            return Result.error("交易记录不存在");
        }
        return Result.success(record);
    }

    @GetMapping("/customer/{customerId}")
    public Result<Page<TransactionRecord>> findByCustomerId(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(transactionService.findByCustomerId(customerId, page, size));
    }

    @GetMapping("/customer/{customerId}/all")
    public Result<List<TransactionRecord>> findAllByCustomerId(@PathVariable Long customerId) {
        return Result.success(transactionService.findByCustomerId(customerId));
    }

    @GetMapping("/order-id/{orderId}")
    public Result<List<TransactionRecord>> findByOrderId(@PathVariable Long orderId) {
        return Result.success(transactionService.findByOrderId(orderId));
    }
}
