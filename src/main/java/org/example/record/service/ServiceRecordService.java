package org.example.record.service;

import org.example.record.entity.ServiceRecord;
import org.example.record.repository.ServiceRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceRecordService {
    
    private final ServiceRecordRepository recordRepository;

    public ServiceRecordService(ServiceRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public List<ServiceRecord> findAll() {
        return recordRepository.findAll();
    }

    public Optional<ServiceRecord> findById(Long id) {
        return recordRepository.findById(id);
    }

    public List<ServiceRecord> findByOrderId(Long orderId) {
        return recordRepository.findByOrderId(orderId);
    }

    public List<ServiceRecord> findByMemberId(Long memberId) {
        return recordRepository.findByMemberId(memberId);
    }

    public List<ServiceRecord> findByStoreId(Long storeId) {
        return recordRepository.findByStoreId(storeId);
    }

    public List<ServiceRecord> findByEmployeeId(Long employeeId) {
        return recordRepository.findByEmployeeId(employeeId);
    }

    @Transactional
    public ServiceRecord create(ServiceRecord record) {
        return recordRepository.save(record);
    }

    @Transactional
    public ServiceRecord receiveClothes(Long orderItemId, Long orderId, Long memberId, 
                                        Long storeId, String clothesName, String clothesImage,
                                        String beforeImages) {
        ServiceRecord record = new ServiceRecord();
        record.setOrderItemId(orderItemId);
        record.setOrderId(orderId);
        record.setMemberId(memberId);
        record.setStoreId(storeId);
        record.setClothesName(clothesName);
        record.setClothesImage(clothesImage);
        record.setBeforeImages(beforeImages);
        record.setReceiveTime(LocalDateTime.now());
        record.setStatus("RECEIVED");
        return recordRepository.save(record);
    }

    @Transactional
    public boolean startService(Long recordId, Long employeeId) {
        return recordRepository.findById(recordId).map(record -> {
            record.setEmployeeId(employeeId);
            record.setStartTime(LocalDateTime.now());
            record.setStatus("IN_PROGRESS");
            recordRepository.save(record);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean endService(Long recordId, String afterImages) {
        return recordRepository.findById(recordId).map(record -> {
            record.setEndTime(LocalDateTime.now());
            record.setAfterImages(afterImages);
            record.setStatus("COMPLETED");
            
            if (record.getStartTime() != null) {
                Duration duration = Duration.between(record.getStartTime(), record.getEndTime());
                record.setActualDuration((int) duration.toMinutes());
            }
            recordRepository.save(record);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean deliverClothes(Long recordId) {
        return recordRepository.findById(recordId).map(record -> {
            record.setDeliveryTime(LocalDateTime.now());
            record.setStatus("DELIVERED");
            recordRepository.save(record);
            return true;
        }).orElse(false);
    }

    public Integer calculateServiceDuration(Long recordId) {
        return recordRepository.findById(recordId)
                .filter(r -> r.getStartTime() != null && r.getEndTime() != null)
                .map(r -> {
                    Duration duration = Duration.between(r.getStartTime(), r.getEndTime());
                    return (int) duration.toMinutes();
                }).orElse(0);
    }
}
