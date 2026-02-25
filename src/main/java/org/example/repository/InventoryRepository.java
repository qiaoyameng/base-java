package org.example.repository;

import org.example.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByStoreId(Long storeId);
    List<Inventory> findByStoreIdAndCategory(Long storeId, String category);
    List<Inventory> findByQuantityLessThan(Integer quantity);
}
