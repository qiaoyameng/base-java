package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.common.PageResult;
import org.example.common.Result;
import org.example.dto.ReviewDTO;
import org.example.entity.ConsumptionRecord;
import org.example.entity.ElectronicReceipt;
import org.example.entity.ProductReview;
import org.example.service.ConsumptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/consumptions")
@RequiredArgsConstructor
public class ConsumptionController {
    private final ConsumptionService consumptionService;

    @GetMapping("/member/{memberId}")
    public Result<PageResult<ConsumptionRecord>> getMemberConsumptions(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<ConsumptionRecord> records = consumptionService.getMemberConsumptions(memberId, pageRequest);
        return Result.success(PageResult.of(records));
    }

    @GetMapping("/member/{memberId}/stats")
    public Result<Map<String, Object>> getMemberStats(@PathVariable Long memberId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalConsumption", consumptionService.getTotalConsumption(memberId));
        stats.put("totalPointsEarned", consumptionService.getTotalPointsEarned(memberId));
        return Result.success(stats);
    }

    @PostMapping("/reviews")
    public Result<ProductReview> createReview(
            @RequestParam Long memberId,
            @Valid @RequestBody ReviewDTO dto) {
        return Result.success(consumptionService.createReview(memberId, dto));
    }

    @GetMapping("/products/{productId}/reviews")
    public Result<PageResult<ProductReview>> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<ProductReview> reviews = consumptionService.getProductReviews(productId, pageRequest);
        return Result.success(PageResult.of(reviews));
    }

    @GetMapping("/member/{memberId}/reviews")
    public Result<PageResult<ProductReview>> getMemberReviews(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<ProductReview> reviews = consumptionService.getMemberReviews(memberId, pageRequest);
        return Result.success(PageResult.of(reviews));
    }

    @GetMapping("/products/{productId}/rating")
    public Result<Map<String, Object>> getProductRating(@PathVariable Long productId) {
        Map<String, Object> rating = new HashMap<>();
        rating.put("averageRating", consumptionService.getAverageRating(productId));
        rating.put("reviewCount", consumptionService.getReviewCount(productId));
        return Result.success(rating);
    }

    @DeleteMapping("/reviews/{id}")
    public Result<Void> deleteReview(@PathVariable Long id, @RequestParam Long memberId) {
        consumptionService.deleteReview(id, memberId);
        return Result.success();
    }

    @PostMapping("/receipts/generate/{orderId}")
    public Result<ElectronicReceipt> generateReceipt(@PathVariable Long orderId) {
        return Result.success(consumptionService.generateReceipt(orderId));
    }

    @GetMapping("/receipts/order/{orderId}")
    public Result<ElectronicReceipt> getReceiptByOrderId(@PathVariable Long orderId) {
        return Result.success(consumptionService.getReceiptByOrderId(orderId));
    }

    @GetMapping("/receipts/{receiptNo}")
    public Result<ElectronicReceipt> getReceiptByNo(@PathVariable String receiptNo) {
        return Result.success(consumptionService.getReceiptByReceiptNo(receiptNo));
    }

    @GetMapping("/member/{memberId}/receipts/{orderId}")
    public Result<ElectronicReceipt> getMemberReceipt(
            @PathVariable Long memberId,
            @PathVariable Long orderId) {
        return Result.success(consumptionService.getMemberReceipt(memberId, orderId));
    }
}
