package com.washshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.washshop.dto.EmployeeDTO;
import com.washshop.entity.Employee;
import com.washshop.entity.Store;
import com.washshop.entity.User;
import com.washshop.mapper.EmployeeMapper;
import com.washshop.mapper.StoreMapper;
import com.washshop.mapper.UserMapper;
import com.washshop.service.EmployeeService;
import com.washshop.vo.EmployeeVO;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<Void> createEmployee(EmployeeDTO dto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);
        save(employee);
        return Result.success();
    }

    @Override
    public Result<Void> updateEmployee(Long id, EmployeeDTO dto) {
        Employee employee = getById(id);
        if (employee == null) {
            return Result.error("员工不存在");
        }
        BeanUtils.copyProperties(dto, employee);
        employee.setId(id);
        updateById(employee);
        return Result.success();
    }

    @Override
    public Result<Void> deleteEmployee(Long id) {
        removeById(id);
        return Result.success();
    }

    @Override
    public Result<EmployeeVO> getEmployeeById(Long id) {
        Employee employee = getById(id);
        if (employee == null) {
            return Result.error("员工不存在");
        }
        return Result.success(convertToVO(employee));
    }

    @Override
    public Result<PageVO<EmployeeVO>> getEmployeePage(Long storeId, Integer status, Long current, Long size) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        if (storeId != null) {
            wrapper.eq(Employee::getStoreId, storeId);
        }
        if (status != null) {
            wrapper.eq(Employee::getStatus, status);
        }
        wrapper.orderByDesc(Employee::getCreateTime);

        Page<Employee> page = new Page<>(current, size);
        page(page, wrapper);

        List<EmployeeVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        PageVO<EmployeeVO> pageVO = new PageVO<>(page.getTotal(), current, size, voList);
        return Result.success(pageVO);
    }

    @Override
    public Result<List<EmployeeVO>> getEmployeesByStore(Long storeId) {
        List<Employee> list = baseMapper.selectByStoreId(storeId);
        List<EmployeeVO> voList = list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        return Result.success(voList);
    }

    @Override
    public Result<EmployeeVO> getEmployeeByUserId(Long userId) {
        Employee employee = baseMapper.selectByUserId(userId);
        if (employee == null) {
            return Result.error("员工不存在");
        }
        return Result.success(convertToVO(employee));
    }

    private EmployeeVO convertToVO(Employee employee) {
        EmployeeVO vo = new EmployeeVO();
        BeanUtils.copyProperties(employee, vo);

        Store store = storeMapper.selectById(employee.getStoreId());
        if (store != null) {
            vo.setStoreName(store.getStoreName());
        }

        User user = userMapper.selectById(employee.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
            vo.setRealName(user.getRealName());
        }

        return vo;
    }
}
