package org.example.order.service;

import org.example.order.entity.RefundRecord;
import org.example.order.repository.RefundRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefundService {
    
    private final RefundRecordRepository refundRecordRepository;
    private final OrderService orderService;

    public RefundService(RefundRecordRepository refundRecordRepository, OrderService orderService) {
        this.refundRecordRepository = refundRecordRepository;
        this.orderService = orderService;
    }

    public List<RefundRecord> findAll() {
        return refundRecordRepository.findAll();
    }

    public Optional<RefundRecord> findById(Long id) {
        return refundRecordRepository.findById(id);
    }

    public Optional<RefundRecord> findByRefundNo(String refundNo) {
        return refundRecordRepository.findByRefundNo(refundNo);
    }

    public List<RefundRecord> findByOrderId(Long orderId) {
        return refundRecordRepository.findByOrderId(orderId);
    }

    public List<RefundRecord> findByMemberId(Long memberId) {
        return refundRecordRepository.findByMemberId(memberId);
    }

    public List<RefundRecord> findPendingRefunds() {
        return refundRecordRepository.findByStatus(0);
    }

    @Transactional
    public RefundRecord applyRefund(Long orderId, Long memberId, BigDecimal amount, String reason) {
        RefundRecord record = new RefundRecord();
        record.setOrderId(orderId);
        record.setMemberId(memberId);
        record.setRefundNo(generateRefundNo());
        record.setRefundAmount(amount);
        record.setReason(reason);
        record.setStatus(0);
        record.setApplyTime(LocalDateTime.now());
        return refundRecordRepository.save(record);
    }

    @Transactional
    public boolean auditRefund(Long refundId, Long auditorId, boolean approved, String remark) {
        return refundRecordRepository.findById(refundId).map(record -> {
            if (record.getStatus() != 0) {
                return false;
            }
            record.setAuditorId(auditorId);
            record.setAuditRemark(remark);
            record.setAuditTime(LocalDateTime.now());
            
            if (approved) {
                record.setStatus(1);
                record.setRefundTime(LocalDateTime.now());
                refundRecordRepository.save(record);
                orderService.cancelOrder(record.getOrderId(), auditorId, "ADMIN", "退款审核通过");
            } else {
                record.setStatus(2);
                refundRecordRepository.save(record);
            }
            return true;
        }).orElse(false);
    }

    private String generateRefundNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomStr = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "REF" + dateStr + randomStr;
    }
}
