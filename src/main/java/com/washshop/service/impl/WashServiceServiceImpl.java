package com.washshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.washshop.dto.WashServiceDTO;
import com.washshop.entity.WashService;
import com.washshop.mapper.WashServiceMapper;
import com.washshop.service.WashServiceService;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WashServiceServiceImpl extends ServiceImpl<WashServiceMapper, WashService> implements WashServiceService {

    @Override
    public Result<Void> createService(WashServiceDTO dto) {
        WashService service = new WashService();
        BeanUtils.copyProperties(dto, service);
        save(service);
        return Result.success();
    }

    @Override
    public Result<Void> updateService(Long id, WashServiceDTO dto) {
        WashService service = getById(id);
        if (service == null) {
            return Result.error("服务不存在");
        }
        BeanUtils.copyProperties(dto, service);
        service.setId(id);
        updateById(service);
        return Result.success();
    }

    @Override
    public Result<Void> deleteService(Long id) {
        removeById(id);
        return Result.success();
    }

    @Override
    public Result<WashService> getServiceById(Long id) {
        WashService service = getById(id);
        if (service == null) {
            return Result.error("服务不存在");
        }
        return Result.success(service);
    }

    @Override
    public Result<PageVO<WashService>> getServicePage(Long categoryId, Integer status, Long current, Long size) {
        LambdaQueryWrapper<WashService> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) {
            wrapper.eq(WashService::getCategoryId, categoryId);
        }
        if (status != null) {
            wrapper.eq(WashService::getStatus, status);
        }
        wrapper.orderByAsc(WashService::getSortOrder);
        
        Page<WashService> page = new Page<>(current, size);
        page(page, wrapper);
        
        PageVO<WashService> pageVO = new PageVO<>(page.getTotal(), current, size, page.getRecords());
        return Result.success(pageVO);
    }

    @Override
    public Result<List<WashService>> getServicesByCategory(Long categoryId) {
        List<WashService> list = baseMapper.selectByCategoryId(categoryId);
        return Result.success(list);
    }

    @Override
    public Result<List<WashService>> getActiveServices() {
        List<WashService> list = baseMapper.selectActiveServices();
        return Result.success(list);
    }
}
