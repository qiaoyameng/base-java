package org.example.service.repository;

import org.example.common.enums.ServiceCategory;
import org.example.common.enums.ServiceStatus;
import org.example.service.entity.LaundryService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LaundryServiceRepository extends JpaRepository<LaundryService, Long>, JpaSpecificationExecutor<LaundryService> {
    
    List<LaundryService> findByCategory(ServiceCategory category);
    
    List<LaundryService> findByStatus(ServiceStatus status);
    
    List<LaundryService> findByStoreId(Long storeId);
    
    List<LaundryService> findByStoreIdAndActiveTrue(Long storeId);
    
    Optional<LaundryService> findByServiceCode(String serviceCode);
    
    List<LaundryService> findByActiveTrue();
    
    List<LaundryService> findByCategoryAndActiveTrue(ServiceCategory category);
}
