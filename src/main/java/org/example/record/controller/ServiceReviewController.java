package org.example.record.controller;

import org.example.common.Result;
import org.example.record.entity.ServiceReview;
import org.example.record.service.ServiceReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-reviews")
public class ServiceReviewController {
    
    private final ServiceReviewService reviewService;

    public ServiceReviewController(ServiceReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public Result<List<ServiceReview>> list() {
        return Result.success(reviewService.findAll());
    }

    @GetMapping("/page")
    public Result<Page<ServiceReview>> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(reviewService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public Result<ServiceReview> getById(@PathVariable Long id) {
        return reviewService.findById(id)
                .map(Result::success)
                .orElse(Result.error("评价不存在"));
    }

    @GetMapping("/order/{orderId}")
    public Result<List<ServiceReview>> getByOrder(@PathVariable Long orderId) {
        return Result.success(reviewService.findByOrderId(orderId));
    }

    @GetMapping("/member/{memberId}")
    public Result<List<ServiceReview>> getByMember(@PathVariable Long memberId) {
        return Result.success(reviewService.findByMemberId(memberId));
    }

    @GetMapping("/store/{storeId}")
    public Result<List<ServiceReview>> getByStore(@PathVariable Long storeId) {
        return Result.success(reviewService.findByStoreId(storeId));
    }

    @GetMapping("/employee/{employeeId}")
    public Result<List<ServiceReview>> getByEmployee(@PathVariable Long employeeId) {
        return Result.success(reviewService.findByEmployeeId(employeeId));
    }

    @PostMapping
    public Result<ServiceReview> create(@RequestBody ServiceReview review) {
        return Result.success(reviewService.createReview(review));
    }

    @PostMapping("/submit")
    public Result<ServiceReview> submitReview(
            @RequestParam Long orderId,
            @RequestParam Long memberId,
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) Long employeeId,
            @RequestParam Integer rating,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String images,
            @RequestParam(required = false, defaultValue = "false") Boolean anonymous) {
        ServiceReview review = reviewService.submitReview(
                orderId, memberId, storeId, employeeId, rating, content, images, anonymous);
        if (review == null) {
            return Result.error("该订单已评价");
        }
        return Result.success(review);
    }

    @PostMapping("/{id}/reply")
    public Result<Void> replyReview(@PathVariable Long id, @RequestParam String reply) {
        if (reviewService.replyReview(id, reply)) {
            return Result.success();
        }
        return Result.error("评价不存在");
    }

    @GetMapping("/store/{storeId}/average-rating")
    public Result<Double> getStoreAverageRating(@PathVariable Long storeId) {
        return Result.success(reviewService.getAverageRatingByStore(storeId));
    }

    @GetMapping("/employee/{employeeId}/average-rating")
    public Result<Double> getEmployeeAverageRating(@PathVariable Long employeeId) {
        return Result.success(reviewService.getAverageRatingByEmployee(employeeId));
    }
}
