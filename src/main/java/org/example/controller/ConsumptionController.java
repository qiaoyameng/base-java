package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.ConsumptionRecordDTO;
import org.example.dto.ReceiptDTO;
import org.example.dto.ReviewDTO;
import org.example.common.PageResult;
import org.example.common.Result;
import org.example.service.ConsumptionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consumption")
@RequiredArgsConstructor
@Tag(name = "消费记录", description = "消费记录查询、评价管理、电子凭证等接口")
public class ConsumptionController {

    private final ConsumptionService consumptionService;

    @GetMapping("/records/customer/{customerId}")
    @Operation(summary = "查询顾客消费记录", description = "分页查询指定顾客的消费记录")
    public Result<PageResult<ConsumptionRecordDTO>> getCustomerConsumptionRecords(
            @Parameter(description = "顾客ID") @PathVariable Long customerId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(consumptionService.getCustomerConsumptionRecords(customerId, pageNum, pageSize));
    }

    @GetMapping("/records/customer/{customerId}/total")
    @Operation(summary = "查询顾客总消费", description = "查询指定顾客的总消费金额")
    public Result<Double> getTotalConsumption(
            @Parameter(description = "顾客ID") @PathVariable Long customerId) {
        return Result.success(consumptionService.getTotalConsumption(customerId));
    }

    @PostMapping("/reviews")
    @Operation(summary = "创建评价", description = "顾客对购买的商品进行5星评价，支持文字和图片")
    public Result<ReviewDTO> createReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        return Result.success(consumptionService.createReview(reviewDTO));
    }

    @PostMapping("/reviews/{reviewId}/reply")
    @Operation(summary = "回复评价", description = "商家回复顾客评价")
    public Result<ReviewDTO> replyReview(
            @Parameter(description = "评价ID") @PathVariable Long reviewId,
            @Parameter(description = "回复内容") @RequestParam String reply) {
        return Result.success(consumptionService.replyReview(reviewId, reply));
    }

    @PostMapping("/reviews/{reviewId}/like")
    @Operation(summary = "点赞评价", description = "为评价点赞")
    public Result<Void> likeReview(
            @Parameter(description = "评价ID") @PathVariable Long reviewId) {
        consumptionService.likeReview(reviewId);
        return Result.success();
    }

    @GetMapping("/reviews/product/{productId}")
    @Operation(summary = "查询商品评价", description = "分页查询指定商品的所有评价")
    public Result<PageResult<ReviewDTO>> getProductReviews(
            @Parameter(description = "商品ID") @PathVariable Long productId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(consumptionService.getProductReviews(productId, pageNum, pageSize));
    }

    @GetMapping("/reviews/customer/{customerId}")
    @Operation(summary = "查询顾客评价", description = "分页查询指定顾客的所有评价")
    public Result<PageResult<ReviewDTO>> getCustomerReviews(
            @Parameter(description = "顾客ID") @PathVariable Long customerId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(consumptionService.getCustomerReviews(customerId, pageNum, pageSize));
    }

    @PostMapping("/receipts/order/{orderId}")
    @Operation(summary = "生成电子凭证", description = "为订单生成电子消费凭证/购物小票")
    public Result<ReceiptDTO> generateReceipt(
            @Parameter(description = "订单ID") @PathVariable Long orderId) {
        return Result.success(consumptionService.generateReceipt(orderId));
    }

    @GetMapping("/receipts/order/{orderId}")
    @Operation(summary = "查询订单凭证", description = "根据订单ID查询电子凭证")
    public Result<ReceiptDTO> getReceiptByOrderId(
            @Parameter(description = "订单ID") @PathVariable Long orderId) {
        return Result.success(consumptionService.getReceiptByOrderId(orderId));
    }

    @GetMapping("/receipts/{receiptNo}")
    @Operation(summary = "查询凭证", description = "根据凭证编号查询电子凭证")
    public Result<ReceiptDTO> getReceiptByNo(
            @Parameter(description = "凭证编号") @PathVariable String receiptNo) {
        return Result.success(consumptionService.getReceiptByNo(receiptNo));
    }

    @PostMapping("/receipts/verify")
    @Operation(summary = "验证凭证", description = "验证电子消费凭证")
    public Result<ReceiptDTO> verifyReceipt(
            @Parameter(description = "验证码") @RequestParam String verificationCode) {
        return Result.success(consumptionService.verifyReceipt(verificationCode));
    }
}
