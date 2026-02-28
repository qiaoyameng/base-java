package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.ProductDTO;
import org.example.dto.ProductQueryDTO;
import org.example.common.PageResult;
import org.example.common.Result;
import org.example.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "商品管理", description = "商品的增删改查、状态管理等接口")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "创建商品", description = "创建新商品，包含名称、价格、库存、规格等信息")
    public Result<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        return Result.success(productService.createProduct(productDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新商品", description = "更新商品信息")
    public Result<ProductDTO> updateProduct(
            @Parameter(description = "商品ID") @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDTO) {
        return Result.success(productService.updateProduct(id, productDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品", description = "逻辑删除商品")
    public Result<Void> deleteProduct(
            @Parameter(description = "商品ID") @PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取商品详情", description = "根据ID获取商品详细信息")
    public Result<ProductDTO> getProductById(
            @Parameter(description = "商品ID") @PathVariable Long id) {
        return Result.success(productService.getProductById(id));
    }

    @GetMapping
    @Operation(summary = "查询商品列表", description = "支持分页和条件查询")
    public Result<PageResult<ProductDTO>> listProducts(ProductQueryDTO queryDTO) {
        return Result.success(productService.listProducts(queryDTO));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新商品状态", description = "更新商品状态：在售/下架/缺货/预售")
    public Result<Void> updateProductStatus(
            @Parameter(description = "商品ID") @PathVariable Long id,
            @Parameter(description = "状态值") @RequestParam String status) {
        productService.updateProductStatus(id, status);
        return Result.success();
    }
}
