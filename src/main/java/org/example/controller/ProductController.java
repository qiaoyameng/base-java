package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.Product;
import org.example.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public Result<Page<Product>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(productService.findAll(page, size));
    }

    @GetMapping("/sale")
    public Result<Page<Product>> findOnSale(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(productService.findOnSale(page, size));
    }

    @GetMapping("/category/{categoryId}")
    public Result<Page<Product>> findByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(productService.findByCategory(categoryId, page, size));
    }

    @GetMapping("/search")
    public Result<Page<Product>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(productService.search(keyword, page, size));
    }

    @GetMapping("/top-sales")
    public Result<List<Product>> findTopSales(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(productService.findTopSales(limit));
    }

    @GetMapping("/{id}")
    public Result<Product> findById(@PathVariable Long id) {
        Product product = productService.findById(id);
        if (product == null) {
            return Result.error("商品不存在");
        }
        return Result.success(product);
    }

    @PostMapping
    public Result<Product> create(@RequestBody Product product) {
        return Result.success(productService.save(product));
    }

    @PutMapping("/{id}")
    public Result<Product> update(@PathVariable Long id, @RequestBody Product product) {
        Product updated = productService.update(id, product);
        if (updated == null) {
            return Result.error("商品不存在");
        }
        return Result.success(updated);
    }

    @PatchMapping("/{id}/status")
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam Product.ProductStatus status) {
        productService.updateStatus(id, status);
        return Result.success();
    }

    @PatchMapping("/{id}/stock")
    public Result<Void> updateStock(
            @PathVariable Long id,
            @RequestParam Integer stock) {
        productService.updateStock(id, stock);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return Result.success();
    }
}
