package org.example.service.controller;

import org.example.common.Result;
import org.example.common.enums.ServiceCategory;
import org.example.common.enums.ServiceStatus;
import org.example.service.entity.LaundryService;
import org.example.service.service.LaundryServiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laundry-services")
public class LaundryServiceController {
    
    private final LaundryServiceService laundryServiceService;

    public LaundryServiceController(LaundryServiceService laundryServiceService) {
        this.laundryServiceService = laundryServiceService;
    }

    @GetMapping
    public Result<List<LaundryService>> list() {
        return Result.success(laundryServiceService.findAll());
    }

    @GetMapping("/page")
    public Result<Page<LaundryService>> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(laundryServiceService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public Result<LaundryService> getById(@PathVariable Long id) {
        return laundryServiceService.findById(id)
                .map(Result::success)
                .orElse(Result.error("服务不存在"));
    }

    @GetMapping("/category/{category}")
    public Result<List<LaundryService>> getByCategory(@PathVariable ServiceCategory category) {
        return Result.success(laundryServiceService.findByCategory(category));
    }

    @GetMapping("/status/{status}")
    public Result<List<LaundryService>> getByStatus(@PathVariable ServiceStatus status) {
        return Result.success(laundryServiceService.findByStatus(status));
    }

    @GetMapping("/store/{storeId}")
    public Result<List<LaundryService>> getByStore(@PathVariable Long storeId) {
        return Result.success(laundryServiceService.findByStoreId(storeId));
    }

    @PostMapping
    public Result<LaundryService> create(@RequestBody LaundryService service) {
        if (service.getServiceCode() != null && 
            laundryServiceService.findByServiceCode(service.getServiceCode()).isPresent()) {
            return Result.error("服务编码已存在");
        }
        return Result.success(laundryServiceService.save(service));
    }

    @PutMapping("/{id}")
    public Result<LaundryService> update(@PathVariable Long id, @RequestBody LaundryService service) {
        LaundryService updated = laundryServiceService.update(id, service);
        if (updated == null) {
            return Result.error("服务不存在");
        }
        return Result.success(updated);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam ServiceStatus status) {
        if (laundryServiceService.updateStatus(id, status)) {
            return Result.success();
        }
        return Result.error("服务不存在");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (laundryServiceService.delete(id)) {
            return Result.success();
        }
        return Result.error("服务不存在");
    }
}
