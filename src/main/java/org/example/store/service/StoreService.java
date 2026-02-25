package org.example.store.service;

import org.example.store.entity.Store;
import org.example.store.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    
    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<Store> findAll() {
        return storeRepository.findByDeletedFalse();
    }

    public Page<Store> findAll(Pageable pageable) {
        return storeRepository.findAll(pageable);
    }

    public Optional<Store> findById(Long id) {
        return storeRepository.findById(id);
    }

    @Transactional
    public Store save(Store store) {
        return storeRepository.save(store);
    }

    @Transactional
    public Store update(Long id, Store store) {
        return storeRepository.findById(id).map(existing -> {
            existing.setStoreName(store.getStoreName());
            existing.setAddress(store.getAddress());
            existing.setPhone(store.getPhone());
            existing.setOpeningTime(store.getOpeningTime());
            existing.setClosingTime(store.getClosingTime());
            existing.setDescription(store.getDescription());
            existing.setStatus(store.getStatus());
            existing.setLongitude(store.getLongitude());
            existing.setLatitude(store.getLatitude());
            return storeRepository.save(existing);
        }).orElse(null);
    }

    @Transactional
    public boolean delete(Long id) {
        return storeRepository.findById(id).map(store -> {
            store.setDeleted(true);
            storeRepository.save(store);
            return true;
        }).orElse(false);
    }

    public List<Store> findByStatus(Integer status) {
        return storeRepository.findByStatus(status);
    }
}
