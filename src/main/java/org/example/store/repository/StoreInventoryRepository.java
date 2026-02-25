package org.example.store.repository;

import org.example.store.entity.StoreInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreInventoryRepository extends JpaRepository<StoreInventory, Long>, JpaSpecificationExecutor<StoreInventory> {
    
    List<StoreInventory> findByStoreId(Long storeId);
    
    List<StoreInventory> findByStoreIdAndQuantityLessThan(Long storeId, Integer minQuantity);
}
