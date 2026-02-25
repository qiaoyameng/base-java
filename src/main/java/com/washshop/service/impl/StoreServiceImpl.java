package com.washshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.washshop.dto.StoreDTO;
import com.washshop.entity.Store;
import com.washshop.mapper.StoreMapper;
import com.washshop.service.StoreService;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;
import com.washshop.vo.StoreVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {

    @Override
    public Result<Void> createStore(StoreDTO dto) {
        Store store = new Store();
        BeanUtils.copyProperties(dto, store);
        save(store);
        return Result.success();
    }

    @Override
    public Result<Void> updateStore(Long id, StoreDTO dto) {
        Store store = getById(id);
        if (store == null) {
            return Result.error("门店不存在");
        }
        BeanUtils.copyProperties(dto, store);
        store.setId(id);
        updateById(store);
        return Result.success();
    }

    @Override
    public Result<Void> deleteStore(Long id) {
        removeById(id);
        return Result.success();
    }

    @Override
    public Result<StoreVO> getStoreById(Long id) {
        Store store = getById(id);
        if (store == null) {
            return Result.error("门店不存在");
        }
        return Result.success(convertToVO(store));
    }

    @Override
    public Result<PageVO<StoreVO>> getStorePage(Integer status, Long current, Long size) {
        LambdaQueryWrapper<Store> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Store::getStatus, status);
        }
        wrapper.orderByAsc(Store::getSortOrder);

        Page<Store> page = new Page<>(current, size);
        page(page, wrapper);

        List<StoreVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        PageVO<StoreVO> pageVO = new PageVO<>(page.getTotal(), current, size, voList);
        return Result.success(pageVO);
    }

    @Override
    public Result<List<StoreVO>> getActiveStores() {
        List<Store> stores = baseMapper.selectActiveStores();
        List<StoreVO> voList = stores.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        return Result.success(voList);
    }

    private StoreVO convertToVO(Store store) {
        StoreVO vo = new StoreVO();
        BeanUtils.copyProperties(store, vo);
        return vo;
    }
}
