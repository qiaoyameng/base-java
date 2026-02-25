package org.example.repository;

import org.example.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByEnabled(Boolean enabled);
    List<Store> findByNameContaining(String name);
}
