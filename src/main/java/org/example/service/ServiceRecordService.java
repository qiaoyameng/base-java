package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ServiceRecordResponse;
import org.example.entity.ServiceRecord;
import org.example.mapstruct.ServiceRecordMapper;
import org.example.repository.ServiceRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceRecordService {
    private final ServiceRecordRepository serviceRecordRepository;
    private final ServiceRecordMapper serviceRecordMapper;

    public ServiceRecordResponse getRecord(Long id) {
        ServiceRecord record = serviceRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("服务记录不存在"));
        return serviceRecordMapper.toResponse(record);
    }

    public ServiceRecordResponse getRecordByOrderId(Long orderId) {
        ServiceRecord record = serviceRecordRepository.findByOrderId(orderId).orElse(null);
        return record != null ? serviceRecordMapper.toResponse(record) : null;
    }

    public List<ServiceRecordResponse> getRecordsByCustomer(Long customerId) {
        List<ServiceRecord> records = serviceRecordRepository.findByCustomerId(customerId);
        return serviceRecordMapper.toResponseList(records);
    }

    public List<ServiceRecordResponse> getRecordsByOrderNo(String orderNo) {
        return serviceRecordRepository.findAll().stream()
                .filter(r -> orderNo.equals(r.getOrderNo()))
                .map(serviceRecordMapper::toResponse)
                .toList();
    }
}
