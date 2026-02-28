package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.Receipt;
import org.example.service.ReceiptService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @GetMapping("/no/{receiptNo}")
    public Result<Receipt> findByReceiptNo(@PathVariable String receiptNo) {
        return Result.success(receiptService.findByReceiptNo(receiptNo));
    }

    @GetMapping("/order/{orderNo}")
    public Result<Receipt> findByOrderNo(@PathVariable String orderNo) {
        return Result.success(receiptService.findByOrderNo(orderNo));
    }

    @GetMapping("/order-id/{orderId}")
    public Result<Receipt> findByOrderId(@PathVariable Long orderId) {
        return Result.success(receiptService.findByOrderId(orderId));
    }

    @GetMapping("/customer/{customerId}")
    public Result<Page<Receipt>> findByCustomerId(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(receiptService.findByCustomerId(customerId, page, size));
    }

    @PostMapping("/generate/{orderNo}")
    public Result<Receipt> generateReceipt(@PathVariable String orderNo) {
        return Result.success(receiptService.generateReceiptByOrderNo(orderNo));
    }
}
