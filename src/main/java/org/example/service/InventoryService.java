package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.InventoryRequest;
import org.example.entity.Inventory;
import org.example.entity.Store;
import org.example.exception.BusinessException;
import org.example.repository.InventoryRepository;
import org.example.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public Inventory createInventory(InventoryRequest request) {
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new BusinessException("门店不存在"));
        Inventory inv = new Inventory();
        inv.setStore(store);
        inv.setName(request.getName());
        inv.setCategory(request.getCategory());
        inv.setUnit(request.getUnit());
        inv.setQuantity(request.getQuantity());
        inv.setMinStock(request.getMinStock());
        inv.setDescription(request.getDescription());
        return inventoryRepository.save(inv);
    }

    public List<Inventory> getByStore(Long storeId) {
        return inventoryRepository.findByStoreId(storeId);
    }

    @Transactional
    public Inventory updateInventory(Long id, InventoryRequest request) {
        Inventory inv = inventoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("库存不存在"));
        inv.setName(request.getName());
        inv.setCategory(request.getCategory());
        inv.setUnit(request.getUnit());
        inv.setQuantity(request.getQuantity());
        inv.setMinStock(request.getMinStock());
        inv.setDescription(request.getDescription());
        return inventoryRepository.save(inv);
    }

    @Transactional
    public void adjustStock(Long id, Integer amount, String reason) {
        Inventory inv = inventoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("库存不存在"));
        Integer newQty = inv.getQuantity() + amount;
        if (newQty < 0) {
            throw new BusinessException("库存不足");
        }
        inv.setQuantity(newQty);
        inventoryRepository.save(inv);
        log.info("库存调整: invId={}, amount={}, reason={}", id, amount, reason);
    }

    @Transactional
    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
        log.info("库存删除成功: invId={}", id);
    }
}
