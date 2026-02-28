package org.example.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.Customer;
import org.example.entity.StoredValueLog;
import org.example.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public Result<Customer> register(@RequestBody Customer customer) {
        return Result.success(customerService.register(customer));
    }

    @GetMapping("/{id}")
    public Result<Customer> findById(@PathVariable Long id) {
        return Result.success(customerService.findById(id));
    }

    @GetMapping("/phone/{phone}")
    public Result<Customer> findByPhone(@PathVariable String phone) {
        return Result.success(customerService.findByPhone(phone));
    }

    @GetMapping
    public Result<Page<Customer>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(customerService.findAll(page, size));
    }

    @GetMapping("/level/{level}")
    public Result<Page<Customer>> findByLevel(
            @PathVariable Customer.MemberLevel level,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(customerService.findByLevel(level, page, size));
    }

    @PutMapping("/{id}")
    public Result<Customer> update(@PathVariable Long id, @RequestBody Customer customer) {
        return Result.success(customerService.update(id, customer));
    }

    @PostMapping("/{id}/points/add")
    public Result<Void> addPoints(@PathVariable Long id, @RequestParam int points) {
        customerService.addPoints(id, points);
        return Result.success(null);
    }

    @PostMapping("/{id}/points/deduct")
    public Result<Integer> deductPoints(@PathVariable Long id, @RequestBody DeductPointsRequest request) {
        int deducted = customerService.deductPoints(id, request.getPoints(), request.getOrderAmount());
        return Result.success(deducted);
    }

    @PostMapping("/{id}/recharge")
    public Result<StoredValueLog> recharge(@PathVariable Long id, @RequestBody RechargeRequest request) {
        return Result.success(customerService.recharge(id, request.getAmount(), request.getDescription()));
    }

    @PostMapping("/{id}/stored-value/use")
    public Result<BigDecimal> useStoredValue(@PathVariable Long id, @RequestBody UseStoredValueRequest request) {
        return Result.success(customerService.useStoredValue(id, request.getAmount(), request.getOrderId(), request.getOrderNo()));
    }

    @GetMapping("/{id}/stored-value/logs")
    public Result<Page<StoredValueLog>> findStoredValueLogs(
            @PathVariable Long id,
            @RequestParam(required = false) StoredValueLog.LogType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (type != null) {
            return Result.success(customerService.findStoredValueLogsByType(id, type, page, size));
        }
        return Result.success(customerService.findStoredValueLogs(id, page, size));
    }

    @PutMapping("/{id}/enabled")
    public Result<Void> toggleEnabled(@PathVariable Long id, @RequestParam Boolean enabled) {
        customerService.toggleEnabled(id, enabled);
        return Result.success(null);
    }

    @PutMapping("/{id}/level/upgrade")
    public Result<Customer.MemberLevel> upgradeLevel(@PathVariable Long id, @RequestParam Customer.MemberLevel newLevel) {
        return Result.success(customerService.upgradeLevel(id, newLevel));
    }

    @Data
    public static class DeductPointsRequest {
        private int points;
        private BigDecimal orderAmount;
    }

    @Data
    public static class RechargeRequest {
        private BigDecimal amount;
        private String description;
    }

    @Data
    public static class UseStoredValueRequest {
        private BigDecimal amount;
        private Long orderId;
        private String orderNo;
    }
}
