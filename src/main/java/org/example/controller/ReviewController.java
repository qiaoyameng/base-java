package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.ProductReview;
import org.example.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public Result<ProductReview> createReview(
            @RequestParam Long customerId,
            @RequestParam Long orderItemId,
            @RequestParam Integer rating,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String images,
            @RequestParam(defaultValue = "false") Boolean isAnonymous) {
        try {
            return Result.success(reviewService.createReview(customerId, orderItemId, rating, content, images, isAnonymous));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/product/{productId}")
    public Result<Page<ProductReview>> findByProductId(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(reviewService.findByProductId(productId, page, size));
    }

    @GetMapping("/product/{productId}/all")
    public Result<List<ProductReview>> findAllByProductId(@PathVariable Long productId) {
        return Result.success(reviewService.findByProductId(productId));
    }

    @GetMapping("/customer/{customerId}")
    public Result<Page<ProductReview>> findByCustomerId(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(reviewService.findByCustomerId(customerId, page, size));
    }

    @GetMapping("/order/{orderId}")
    public Result<List<ProductReview>> findByOrderId(@PathVariable Long orderId) {
        return Result.success(reviewService.findByOrderId(orderId));
    }

    @GetMapping("/{id}")
    public Result<ProductReview> findById(@PathVariable Long id) {
        ProductReview review = reviewService.findById(id);
        if (review == null) {
            return Result.error("评价不存在");
        }
        return Result.success(review);
    }

    @PostMapping("/{id}/reply")
    public Result<ProductReview> reply(
            @PathVariable Long id,
            @RequestParam String reply) {
        ProductReview review = reviewService.reply(id, reply);
        if (review == null) {
            return Result.error("评价不存在");
        }
        return Result.success(review);
    }

    @PatchMapping("/{id}/top")
    public Result<Void> toggleTop(
            @PathVariable Long id,
            @RequestParam Boolean isTop) {
        reviewService.toggleTop(id, isTop);
        return Result.success();
    }

    @PatchMapping("/{id}/enabled")
    public Result<Void> toggleEnabled(
            @PathVariable Long id,
            @RequestParam Boolean enabled) {
        reviewService.toggleEnabled(id, enabled);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return Result.success();
    }
}
