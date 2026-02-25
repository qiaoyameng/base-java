package com.washshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.washshop.dto.WashServiceDTO;
import com.washshop.entity.WashService;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;

import java.util.List;

public interface WashServiceService extends IService<WashService> {

    Result<Void> createService(WashServiceDTO dto);

    Result<Void> updateService(Long id, WashServiceDTO dto);

    Result<Void> deleteService(Long id);

    Result<WashService> getServiceById(Long id);

    Result<PageVO<WashService>> getServicePage(Long categoryId, Integer status, Long current, Long size);

    Result<List<WashService>> getServicesByCategory(Long categoryId);

    Result<List<WashService>> getActiveServices();
}
