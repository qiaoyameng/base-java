package org.example.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.PhotoWall;
import org.example.service.PhotoWallService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoWallController {

    private final PhotoWallService photoWallService;

    @PostMapping
    public Result<PhotoWall> upload(@RequestBody PhotoRequest request) {
        PhotoWall photo = photoWallService.uploadPhoto(
                request.getCustomerId(),
                request.getProductId(),
                request.getImage(),
                request.getDescription()
        );
        return Result.success(photo);
    }

    @GetMapping
    public Result<Page<PhotoWall>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.success(photoWallService.findAll(page, size));
    }

    @GetMapping("/customer/{customerId}")
    public Result<Page<PhotoWall>> findByCustomerId(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.success(photoWallService.findByCustomerId(customerId, page, size));
    }

    @GetMapping("/product/{productId}")
    public Result<Page<PhotoWall>> findByProductId(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.success(photoWallService.findByProductId(productId, page, size));
    }

    @GetMapping("/top")
    public Result<List<PhotoWall>> findTopPhotos() {
        return Result.success(photoWallService.findTopPhotos());
    }

    @GetMapping("/{id}")
    public Result<PhotoWall> findById(@PathVariable Long id) {
        return Result.success(photoWallService.findById(id));
    }

    @PutMapping("/{id}")
    public Result<PhotoWall> update(@PathVariable Long id, @RequestBody PhotoRequest request) {
        return Result.success(photoWallService.updatePhoto(id, request.getDescription()));
    }

    @PostMapping("/{id}/like")
    public Result<Void> like(@PathVariable Long id) {
        photoWallService.like(id);
        return Result.success(null);
    }

    @PutMapping("/{id}/top")
    public Result<Void> toggleTop(@PathVariable Long id, @RequestParam Boolean isTop) {
        photoWallService.toggleTop(id, isTop);
        return Result.success(null);
    }

    @PutMapping("/{id}/enabled")
    public Result<Void> toggleEnabled(@PathVariable Long id, @RequestParam Boolean enabled) {
        photoWallService.toggleEnabled(id, enabled);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        photoWallService.delete(id);
        return Result.success(null);
    }

    @Data
    public static class PhotoRequest {
        private Long customerId;
        private Long productId;
        private String image;
        private String description;
    }
}
