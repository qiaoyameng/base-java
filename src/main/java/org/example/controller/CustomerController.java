package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.dto.CustomerResponse;
import org.example.service.CustomerService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "客户管理")
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @Operation(summary = "获取我的客户信息")
    @GetMapping("/me")
    public Result<CustomerResponse> getMyInfo() {
        return Result.success(customerService.toResponse(customerService.getCustomerById(1L)));
    }

    @Operation(summary = "获取客户信息")
    @GetMapping("/{id}")
    public Result<CustomerResponse> getCustomer(@PathVariable Long id) {
        return Result.success(customerService.toResponse(customerService.getCustomerById(id)));
    }

    @Operation(summary = "更新我的会员等级(根据积分自动计算)")
    @PostMapping("/me/level")
    public Result<Void> updateMyLevel() {
        customerService.updateMemberLevel(1L);
        return Result.success();
    }
}
