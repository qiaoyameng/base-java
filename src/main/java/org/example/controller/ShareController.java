package org.example.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.Share;
import org.example.service.ShareService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shares")
@RequiredArgsConstructor
public class ShareController {

    private final ShareService shareService;

    @PostMapping
    public Result<Share> create(@RequestBody ShareRequest request) {
        Share share = shareService.createShare(
                request.getCustomerId(),
                request.getTitle(),
                request.getContent(),
                request.getImages(),
                request.getProductId(),
                request.getTags()
        );
        return Result.success(share);
    }

    @GetMapping
    public Result<Page<Share>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(shareService.findAll(page, size));
    }

    @GetMapping("/customer/{customerId}")
    public Result<Page<Share>> findByCustomerId(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(shareService.findByCustomerId(customerId, page, size));
    }

    @GetMapping("/product/{productId}")
    public Result<Page<Share>> findByProductId(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(shareService.findByProductId(productId, page, size));
    }

    @GetMapping("/top")
    public Result<List<Share>> findTopShares() {
        return Result.success(shareService.findTopShares());
    }

    @GetMapping("/tag")
    public Result<List<Share>> findByTag(@RequestParam String tag) {
        return Result.success(shareService.findByTag(tag));
    }

    @GetMapping("/{id}")
    public Result<Share> findById(@PathVariable Long id) {
        return Result.success(shareService.findById(id));
    }

    @PutMapping("/{id}")
    public Result<Share> update(@PathVariable Long id, @RequestBody ShareRequest request) {
        return Result.success(shareService.updateShare(id, request.getTitle(), request.getContent(), request.getImages(), request.getTags()));
    }

    @PostMapping("/{id}/like")
    public Result<Void> like(@PathVariable Long id) {
        shareService.like(id);
        return Result.success(null);
    }

    @PostMapping("/{id}/view")
    public Result<Void> view(@PathVariable Long id) {
        shareService.view(id);
        return Result.success(null);
    }

    @PutMapping("/{id}/top")
    public Result<Void> toggleTop(@PathVariable Long id, @RequestParam Boolean isTop) {
        shareService.toggleTop(id, isTop);
        return Result.success(null);
    }

    @PutMapping("/{id}/enabled")
    public Result<Void> toggleEnabled(@PathVariable Long id, @RequestParam Boolean enabled) {
        shareService.toggleEnabled(id, enabled);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        shareService.delete(id);
        return Result.success(null);
    }

    @Data
    public static class ShareRequest {
        private Long customerId;
        private String title;
        private String content;
        private String images;
        private Long productId;
        private String tags;
    }
}
