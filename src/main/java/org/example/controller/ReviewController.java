package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.dto.ReviewRequest;
import org.example.dto.ReviewResponse;
import org.example.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "评价管理")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "创建评价")
    @PostMapping
    public Result<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest request) {
        return Result.success(reviewService.createReview(request));
    }

    @Operation(summary = "获取评价详情")
    @GetMapping("/{id}")
    public Result<ReviewResponse> getReview(@PathVariable Long id) {
        return Result.success(reviewService.getReview(id));
    }

    @Operation(summary = "门店评价列表")
    @GetMapping("/store/{storeId}")
    public Result<List<ReviewResponse>> getReviewsByStore(
            @PathVariable Long storeId,
            @RequestParam(required = false) Integer rating) {
        return Result.success(reviewService.getReviewsByStore(storeId, rating));
    }

    @Operation(summary = "我的评价列表")
    @GetMapping("/my")
    public Result<List<ReviewResponse>> getMyReviews() {
        return Result.success(reviewService.getReviewsByCustomer(1L));
    }

    @Operation(summary = "回复评价")
    @PutMapping("/{id}/reply")
    public Result<ReviewResponse> replyReview(
            @PathVariable Long id,
            @RequestParam String reply) {
        return Result.success(reviewService.replyReview(id, reply));
    }

    @Operation(summary = "删除评价")
    @DeleteMapping("/{id}")
    public Result<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return Result.success();
    }
}
