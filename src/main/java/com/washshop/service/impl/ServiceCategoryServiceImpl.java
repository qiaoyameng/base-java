package com.washshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.washshop.dto.ServiceCategoryDTO;
import com.washshop.entity.ServiceCategory;
import com.washshop.mapper.ServiceCategoryMapper;
import com.washshop.service.ServiceCategoryService;
import com.washshop.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceCategoryServiceImpl extends ServiceImpl<ServiceCategoryMapper, ServiceCategory> implements ServiceCategoryService {

    @Override
    public Result<Void> createCategory(ServiceCategoryDTO dto) {
        ServiceCategory category = new ServiceCategory();
        BeanUtils.copyProperties(dto, category);
        save(category);
        return Result.success();
    }

    @Override
    public Result<Void> updateCategory(Long id, ServiceCategoryDTO dto) {
        ServiceCategory category = getById(id);
        if (category == null) {
            return Result.error("分类不存在");
        }
        BeanUtils.copyProperties(dto, category);
        category.setId(id);
        updateById(category);
        return Result.success();
    }

    @Override
    public Result<Void> deleteCategory(Long id) {
        removeById(id);
        return Result.success();
    }

    @Override
    public Result<ServiceCategory> getCategoryById(Long id) {
        ServiceCategory category = getById(id);
        if (category == null) {
            return Result.error("分类不存在");
        }
        return Result.success(category);
    }

    @Override
    public Result<List<ServiceCategory>> getAllCategories() {
        LambdaQueryWrapper<ServiceCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(ServiceCategory::getSortOrder);
        List<ServiceCategory> list = list(wrapper);
        return Result.success(list);
    }

    @Override
    public Result<List<ServiceCategory>> getActiveCategories() {
        LambdaQueryWrapper<ServiceCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServiceCategory::getStatus, 1);
        wrapper.orderByAsc(ServiceCategory::getSortOrder);
        List<ServiceCategory> list = list(wrapper);
        return Result.success(list);
    }
}
