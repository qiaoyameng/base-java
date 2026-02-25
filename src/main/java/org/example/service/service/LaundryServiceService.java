package org.example.service.service;

import org.example.common.enums.ServiceCategory;
import org.example.common.enums.ServiceStatus;
import org.example.service.entity.LaundryService;
import org.example.service.repository.LaundryServiceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LaundryServiceService {
    
    private final LaundryServiceRepository laundryServiceRepository;

    public LaundryServiceService(LaundryServiceRepository laundryServiceRepository) {
        this.laundryServiceRepository = laundryServiceRepository;
    }

    public List<LaundryService> findAll() {
        return laundryServiceRepository.findByActiveTrue();
    }

    public Page<LaundryService> findAll(Pageable pageable) {
        return laundryServiceRepository.findAll(pageable);
    }

    public Optional<LaundryService> findById(Long id) {
        return laundryServiceRepository.findById(id);
    }

    public Optional<LaundryService> findByServiceCode(String serviceCode) {
        return laundryServiceRepository.findByServiceCode(serviceCode);
    }

    public List<LaundryService> findByCategory(ServiceCategory category) {
        return laundryServiceRepository.findByCategoryAndActiveTrue(category);
    }

    public List<LaundryService> findByStatus(ServiceStatus status) {
        return laundryServiceRepository.findByStatus(status);
    }

    public List<LaundryService> findByStoreId(Long storeId) {
        return laundryServiceRepository.findByStoreIdAndActiveTrue(storeId);
    }

    @Transactional
    public LaundryService save(LaundryService service) {
        return laundryServiceRepository.save(service);
    }

    @Transactional
    public LaundryService update(Long id, LaundryService service) {
        return laundryServiceRepository.findById(id).map(existing -> {
            existing.setServiceName(service.getServiceName());
            existing.setCategory(service.getCategory());
            existing.setPrice(service.getPrice());
            existing.setEstimatedDuration(service.getEstimatedDuration());
            existing.setDescription(service.getDescription());
            existing.setImageUrl(service.getImageUrl());
            existing.setSortOrder(service.getSortOrder());
            return laundryServiceRepository.save(existing);
        }).orElse(null);
    }

    @Transactional
    public boolean updateStatus(Long id, ServiceStatus status) {
        return laundryServiceRepository.findById(id).map(service -> {
            service.setStatus(status);
            laundryServiceRepository.save(service);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean delete(Long id) {
        return laundryServiceRepository.findById(id).map(service -> {
            service.setDeleted(true);
            service.setActive(false);
            laundryServiceRepository.save(service);
            return true;
        }).orElse(false);
    }
}
