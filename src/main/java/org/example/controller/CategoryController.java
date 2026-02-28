package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.Category;
import org.example.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Result<List<Category>> findAll() {
        return Result.success(categoryService.findAll());
    }

    @GetMapping("/enabled")
    public Result<List<Category>> findEnabled() {
        return Result.success(categoryService.findEnabled());
    }

    @GetMapping("/{id}")
    public Result<Category> findById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return Result.error("分类不存在");
        }
        return Result.success(category);
    }

    @PostMapping
    public Result<Category> create(@RequestBody Category category) {
        return Result.success(categoryService.save(category));
    }

    @PutMapping("/{id}")
    public Result<Category> update(@PathVariable Long id, @RequestBody Category category) {
        Category updated = categoryService.update(id, category);
        if (updated == null) {
            return Result.error("分类不存在");
        }
        return Result.success(updated);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.success();
    }
}
