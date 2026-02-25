package org.example.store.controller;

import org.example.common.Result;
import org.example.store.entity.Store;
import org.example.store.service.StoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {
    
    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public Result<List<Store>> list() {
        return Result.success(storeService.findAll());
    }

    @GetMapping("/page")
    public Result<Page<Store>> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(storeService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public Result<Store> getById(@PathVariable Long id) {
        return storeService.findById(id)
                .map(Result::success)
                .orElse(Result.error("门店不存在"));
    }

    @PostMapping
    public Result<Store> create(@RequestBody Store store) {
        return Result.success(storeService.save(store));
    }

    @PutMapping("/{id}")
    public Result<Store> update(@PathVariable Long id, @RequestBody Store store) {
        Store updated = storeService.update(id, store);
        if (updated == null) {
            return Result.error("门店不存在");
        }
        return Result.success(updated);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (storeService.delete(id)) {
            return Result.success();
        }
        return Result.error("门店不存在");
    }

    @GetMapping("/status/{status}")
    public Result<List<Store>> getByStatus(@PathVariable Integer status) {
        return Result.success(storeService.findByStatus(status));
    }
}
