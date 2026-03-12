package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.common.PageResult;
import org.example.common.Result;
import org.example.dto.ProductDTO;
import org.example.entity.Product;
import org.example.enums.ProductCategory;
import org.example.enums.ProductStatus;
import org.example.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public Result<Product> createProduct(@Valid @RequestBody ProductDTO dto) {
        return Result.success(productService.createProduct(dto));
    }

    @PutMapping("/{id}")
    public Result<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return Result.success(productService.updateProduct(id, dto));
    }

    @GetMapping("/{id}")
    public Result<Product> getProduct(@PathVariable Long id) {
        return Result.success(productService.getProductById(id));
    }

    @GetMapping
    public Result<PageResult<Product>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") 
                ? Sort.by(sortBy).ascending() 
                : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<Product> products = productService.getProducts(pageRequest);
        return Result.success(PageResult.of(products));
    }

    @GetMapping("/category/{category}")
    public Result<PageResult<Product>> getProductsByCategory(
            @PathVariable ProductCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<Product> products = productService.getProductsByCategory(category, pageRequest);
        return Result.success(PageResult.of(products));
    }

    @GetMapping("/status/{status}")
    public Result<PageResult<Product>> getProductsByStatus(
            @PathVariable ProductStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<Product> products = productService.getProductsByStatus(status, pageRequest);
        return Result.success(PageResult.of(products));
    }

    @GetMapping("/search")
    public Result<PageResult<Product>> searchProducts(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<Product> products = productService.searchProducts(name, pageRequest);
        return Result.success(PageResult.of(products));
    }

    @PutMapping("/{id}/stock")
    public Result<Void> updateStock(@PathVariable Long id, @RequestParam Integer quantity) {
        productService.updateStock(id, quantity);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam ProductStatus status) {
        productService.updateStatus(id, status);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success();
    }

    @GetMapping("/low-stock")
    public Result<?> getLowStockProducts(@RequestParam(defaultValue = "10") Integer threshold) {
        return Result.success(productService.getLowStockProducts(threshold));
    }
}
