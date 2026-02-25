package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.StoreRequest;
import org.example.dto.StoreResponse;
import org.example.entity.Store;
import org.example.exception.BusinessException;
import org.example.mapstruct.StoreMapper;
import org.example.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    @Transactional
    public StoreResponse createStore(StoreRequest request) {
        Store store = storeMapper.toEntity(request);
        store.setEnabled(true);
        Store saved = storeRepository.save(store);
        log.info("门店创建成功: storeId={}, name={}", saved.getId(), request.getName());
        return storeMapper.toResponse(saved);
    }

    public StoreResponse getStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("门店不存在"));
        return storeMapper.toResponse(store);
    }

    public List<StoreResponse> getAllStores() {
        return storeMapper.toResponseList(storeRepository.findByEnabled(true));
    }

    @Transactional
    public StoreResponse updateStore(Long id, StoreRequest request) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("门店不存在"));
        store.setName(request.getName());
        store.setAddress(request.getAddress());
        store.setPhone(request.getPhone());
        store.setBusinessHours(request.getBusinessHours());
        store.setDescription(request.getDescription());
        store.setLatitude(request.getLatitude());
        store.setLongitude(request.getLongitude());
        Store saved = storeRepository.save(store);
        return storeMapper.toResponse(saved);
    }

    @Transactional
    public void deleteStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("门店不存在"));
        store.setEnabled(false);
        storeRepository.save(store);
    }
}
