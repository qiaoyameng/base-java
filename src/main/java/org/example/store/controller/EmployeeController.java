package org.example.store.controller;

import org.example.common.Result;
import org.example.store.entity.Employee;
import org.example.store.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/store/{storeId}")
    public Result<List<Employee>> listByStore(@PathVariable Long storeId) {
        return Result.success(employeeService.findByStoreId(storeId));
    }

    @GetMapping("/page")
    public Result<Page<Employee>> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(employeeService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        return employeeService.findById(id)
                .map(Result::success)
                .orElse(Result.error("员工不存在"));
    }

    @PostMapping
    public Result<Employee> create(@RequestBody Employee employee) {
        if (employeeService.findByPhone(employee.getPhone()).isPresent()) {
            return Result.error("手机号已存在");
        }
        return Result.success(employeeService.save(employee));
    }

    @PutMapping("/{id}")
    public Result<Employee> update(@PathVariable Long id, @RequestBody Employee employee) {
        Employee updated = employeeService.update(id, employee);
        if (updated == null) {
            return Result.error("员工不存在");
        }
        return Result.success(updated);
    }

    @PutMapping("/{id}/password")
    public Result<Void> updatePassword(@PathVariable Long id, @RequestParam String password) {
        if (employeeService.updatePassword(id, password)) {
            return Result.success();
        }
        return Result.error("员工不存在");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (employeeService.delete(id)) {
            return Result.success();
        }
        return Result.error("员工不存在");
    }

    @GetMapping("/store/{storeId}/active")
    public Result<List<Employee>> getActiveByStore(@PathVariable Long storeId) {
        return Result.success(employeeService.findActiveByStoreId(storeId));
    }
}
