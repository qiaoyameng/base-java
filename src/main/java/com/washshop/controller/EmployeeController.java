package com.washshop.controller;

import com.washshop.dto.EmployeeDTO;
import com.washshop.service.EmployeeService;
import com.washshop.vo.EmployeeVO;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@Tag(name = "员工管理", description = "门店员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    @Operation(summary = "创建员工")
    public Result<Void> createEmployee(@RequestBody @Validated EmployeeDTO dto) {
        return employeeService.createEmployee(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新员工")
    public Result<Void> updateEmployee(@PathVariable Long id, @RequestBody @Validated EmployeeDTO dto) {
        return employeeService.updateEmployee(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除员工")
    public Result<Void> deleteEmployee(@PathVariable Long id) {
        return employeeService.deleteEmployee(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取员工详情")
    public Result<EmployeeVO> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询员工")
    public Result<PageVO<EmployeeVO>> getEmployeePage(@RequestParam(required = false) Long storeId,
                                                      @RequestParam(required = false) Integer status,
                                                      @RequestParam(defaultValue = "1") Long current,
                                                      @RequestParam(defaultValue = "10") Long size) {
        return employeeService.getEmployeePage(storeId, status, current, size);
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "获取门店员工")
    public Result<List<EmployeeVO>> getEmployeesByStore(@PathVariable Long storeId) {
        return employeeService.getEmployeesByStore(storeId);
    }

    @GetMapping("/my-info")
    @Operation(summary = "获取我的员工信息")
    public Result<EmployeeVO> getMyEmployeeInfo(@RequestAttribute("userId") Long userId) {
        return employeeService.getEmployeeByUserId(userId);
    }
}
