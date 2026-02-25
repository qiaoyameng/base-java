package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.WashServiceRequest;
import org.example.dto.WashServiceResponse;
import org.example.entity.Store;
import org.example.entity.WashService;
import org.example.enums.ServiceCategory;
import org.example.enums.ServiceStatus;
import org.example.exception.BusinessException;
import org.example.mapstruct.WashServiceMapper;
import org.example.repository.StoreRepository;
import org.example.repository.WashServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WashServiceService {

    private final WashServiceRepository washServiceRepository;
    private final StoreRepository storeRepository;
    private final WashServiceMapper washServiceMapper;

    @Transactional
    public WashServiceResponse createService(WashServiceRequest request) {
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new BusinessException("门店不存在"));

        WashService service = washServiceMapper.toEntity(request);
        service.setStore(store);
        service.setStatus(ServiceStatus.PENDING);
        service.setEnabled(true);

        WashService saved = washServiceRepository.save(service);
        log.info("创建洗衣服务成功: serviceId={}, name={}", saved.getId(), saved.getName());
        return washServiceMapper.toResponse(saved);
    }

    @Transactional
    public WashServiceResponse updateService(Long id, WashServiceRequest request) {
        WashService service = washServiceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("服务不存在"));

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new BusinessException("门店不存在"));

        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setCategory(request.getCategory());
        service.setPrice(request.getPrice());
        service.setDurationMinutes(request.getDurationMinutes());
        service.setImage(request.getImage());
        service.setStore(store);

        WashService saved = washServiceRepository.save(service);
        log.info("更新洗衣服务成功: serviceId={}", saved.getId());
        return washServiceMapper.toResponse(saved);
    }

    public WashServiceResponse getServiceById(Long id) {
        WashService service = washServiceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("服务不存在"));
        return washServiceMapper.toResponse(service);
    }

    public List<WashServiceResponse> getAllEnabledServices() {
        List<WashService> services = washServiceRepository.findByEnabled(true);
        return washServiceMapper.toResponseList(services);
    }

    public List<WashServiceResponse> getServicesByCategory(ServiceCategory category) {
        List<WashService> services = washServiceRepository.findByCategoryAndEnabled(category, true);
        return washServiceMapper.toResponseList(services);
    }

    public List<WashServiceResponse> getServicesByStore(Long storeId) {
        List<WashService> services = washServiceRepository.findByStoreIdAndEnabled(storeId, true);
        return washServiceMapper.toResponseList(services);
    }

    @Transactional
    public void deleteService(Long id) {
        WashService service = washServiceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("服务不存在"));
        service.setEnabled(false);
        washServiceRepository.save(service);
        log.info("删除洗衣服务(软删除): serviceId={}", id);
    }

    @Transactional
    public WashServiceResponse updateServiceStatus(Long id, ServiceStatus status) {
        WashService service = washServiceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("服务不存在"));
        service.setStatus(status);
        WashService saved = washServiceRepository.save(service);
        return washServiceMapper.toResponse(saved);
    }
}
