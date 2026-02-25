package com.washshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.washshop.dto.StoreDTO;
import com.washshop.entity.Store;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;
import com.washshop.vo.StoreVO;

import java.util.List;

public interface StoreService extends IService<Store> {

    Result<Void> createStore(StoreDTO dto);

    Result<Void> updateStore(Long id, StoreDTO dto);

    Result<Void> deleteStore(Long id);

    Result<StoreVO> getStoreById(Long id);

    Result<PageVO<StoreVO>> getStorePage(Integer status, Long current, Long size);

    Result<List<StoreVO>> getActiveStores();
}
