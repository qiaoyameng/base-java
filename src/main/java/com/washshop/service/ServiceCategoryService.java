package com.washshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.washshop.dto.ServiceCategoryDTO;
import com.washshop.entity.ServiceCategory;
import com.washshop.vo.Result;

import java.util.List;

public interface ServiceCategoryService extends IService<ServiceCategory> {

    Result<Void> createCategory(ServiceCategoryDTO dto);

    Result<Void> updateCategory(Long id, ServiceCategoryDTO dto);

    Result<Void> deleteCategory(Long id);

    Result<ServiceCategory> getCategoryById(Long id);

    Result<List<ServiceCategory>> getAllCategories();

    Result<List<ServiceCategory>> getActiveCategories();
}
