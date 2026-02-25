package com.washshop.controller;

import com.washshop.dto.ServiceCategoryDTO;
import com.washshop.entity.ServiceCategory;
import com.washshop.service.ServiceCategoryService;
import com.washshop.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-category")
@Tag(name = "服务分类管理", description = "洗衣服务分类相关接口")
public class ServiceCategoryController {

    @Autowired
    private ServiceCategoryService categoryService;

    @PostMapping
    @Operation(summary = "创建服务分类")
    public Result<Void> createCategory(@RequestBody @Validated ServiceCategoryDTO dto) {
        return categoryService.createCategory(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新服务分类")
    public Result<Void> updateCategory(@PathVariable Long id, @RequestBody @Validated ServiceCategoryDTO dto) {
        return categoryService.updateCategory(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除服务分类")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取分类详情")
    public Result<ServiceCategory> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/list")
    @Operation(summary = "获取所有分类")
    public Result<List<ServiceCategory>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/active")
    @Operation(summary = "获取启用的分类")
    public Result<List<ServiceCategory>> getActiveCategories() {
        return categoryService.getActiveCategories();
    }
}
