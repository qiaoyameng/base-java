package org.example.store.service;

import org.example.store.entity.StoreInventory;
import org.example.store.repository.StoreInventoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StoreInventoryService {
    
    private final StoreInventoryRepository inventoryRepository;

    public StoreInventoryService(StoreInventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<StoreInventory> findByStoreId(Long storeId) {
        return inventoryRepository.findByStoreId(storeId);
    }

    public Page<StoreInventory> findAll(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }

    public Optional<StoreInventory> findById(Long id) {
        return inventoryRepository.findById(id);
    }

    @Transactional
    public StoreInventory save(StoreInventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public StoreInventory update(Long id, StoreInventory inventory) {
        return inventoryRepository.findById(id).map(existing -> {
            existing.setMaterialName(inventory.getMaterialName());
            existing.setMaterialType(inventory.getMaterialType());
            existing.setUnit(inventory.getUnit());
            existing.setQuantity(inventory.getQuantity());
            existing.setMinQuantity(inventory.getMinQuantity());
            existing.setUnitPrice(inventory.getUnitPrice());
            existing.setSupplier(inventory.getSupplier());
            return inventoryRepository.save(existing);
        }).orElse(null);
    }

    @Transactional
    public boolean adjustQuantity(Long id, Integer quantity) {
        return inventoryRepository.findById(id).map(inventory -> {
            inventory.setQuantity(inventory.getQuantity() + quantity);
            inventoryRepository.save(inventory);
            return true;
        }).orElse(false);
    }

    public List<StoreInventory> findLowStock(Long storeId) {
        return inventoryRepository.findByStoreIdAndQuantityLessThan(storeId, 10);
    }

    @Transactional
    public boolean delete(Long id) {
        return inventoryRepository.findById(id).map(inventory -> {
            inventory.setDeleted(true);
            inventoryRepository.save(inventory);
            return true;
        }).orElse(false);
    }
}
