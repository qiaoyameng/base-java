package com.washshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.washshop.dto.EmployeeDTO;
import com.washshop.entity.Employee;
import com.washshop.vo.EmployeeVO;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;

import java.util.List;

public interface EmployeeService extends IService<Employee> {

    Result<Void> createEmployee(EmployeeDTO dto);

    Result<Void> updateEmployee(Long id, EmployeeDTO dto);

    Result<Void> deleteEmployee(Long id);

    Result<EmployeeVO> getEmployeeById(Long id);

    Result<PageVO<EmployeeVO>> getEmployeePage(Long storeId, Integer status, Long current, Long size);

    Result<List<EmployeeVO>> getEmployeesByStore(Long storeId);

    Result<EmployeeVO> getEmployeeByUserId(Long userId);
}
