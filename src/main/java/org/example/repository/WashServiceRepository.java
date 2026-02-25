package org.example.repository;

import org.example.entity.WashService;
import org.example.enums.ServiceCategory;
import org.example.enums.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WashServiceRepository extends JpaRepository<WashService, Long> {
    List<WashService> findByCategoryAndEnabled(ServiceCategory category, Boolean enabled);
    List<WashService> findByStoreIdAndEnabled(Long storeId, Boolean enabled);
    List<WashService> findByEnabled(Boolean enabled);
    List<WashService> findByStatus(ServiceStatus status);
}
