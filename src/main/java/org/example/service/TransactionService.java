package org.example.service;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.example.entity.Orders;
import org.example.entity.TransactionRecord;
import org.example.repository.TransactionRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRecordRepository transactionRecordRepository;

    @Transactional
    public TransactionRecord createPurchaseRecord(Orders order) {
        TransactionRecord record = new TransactionRecord();
        record.setTransactionNo(IdUtil.getSnowflakeNextIdStr());
        record.setCustomerId(order.getCustomerId());
        record.setOrderId(order.getId());
        record.setOrderNo(order.getOrderNo());
        record.setType(TransactionRecord.TransactionType.PURCHASE);
        record.setAmount(order.getPaidAmount());
        record.setDescription("商品购买 - " + order.getOrderNo());
        if (order.getPaymentMethod() != null) {
            if (Orders.PaymentMethod.ONLINE.equals(order.getPaymentMethod())) {
                record.setPaymentMethod(TransactionRecord.PaymentMethod.WECHAT);
            } else {
                record.setPaymentMethod(TransactionRecord.PaymentMethod.CASH);
            }
        }
        return transactionRecordRepository.save(record);
    }

    @Transactional
    public TransactionRecord createPointsRecord(Long customerId, int points, String description) {
        TransactionRecord record = new TransactionRecord();
        record.setTransactionNo(IdUtil.getSnowflakeNextIdStr());
        record.setCustomerId(customerId);
        record.setType(TransactionRecord.TransactionType.POINTS_EARN);
        record.setPoints(points);
        record.setDescription(description);
        return transactionRecordRepository.save(record);
    }

    @Transactional
    public TransactionRecord createPointsDeductRecord(Long customerId, Long orderId, String orderNo, int points, BigDecimal amount) {
        TransactionRecord record = new TransactionRecord();
        record.setTransactionNo(IdUtil.getSnowflakeNextIdStr());
        record.setCustomerId(customerId);
        record.setOrderId(orderId);
        record.setOrderNo(orderNo);
        record.setType(TransactionRecord.TransactionType.POINTS_DEDUCT);
        record.setPoints(points);
        record.setAmount(amount);
        record.setDescription("积分抵扣");
        return transactionRecordRepository.save(record);
    }

    @Transactional
    public TransactionRecord createRefundRecord(Orders order) {
        TransactionRecord record = new TransactionRecord();
        record.setTransactionNo(IdUtil.getSnowflakeNextIdStr());
        record.setCustomerId(order.getCustomerId());
        record.setOrderId(order.getId());
        record.setOrderNo(order.getOrderNo());
        record.setType(TransactionRecord.TransactionType.REFUND);
        record.setAmount(order.getPaidAmount().negate());
        record.setDescription("订单退款 - " + order.getOrderNo());
        return transactionRecordRepository.save(record);
    }

    public TransactionRecord findByTransactionNo(String transactionNo) {
        return transactionRecordRepository.findByTransactionNo(transactionNo).orElse(null);
    }

    public TransactionRecord findByOrderNo(String orderNo) {
        return transactionRecordRepository.findByOrderNo(orderNo).orElse(null);
    }

    public List<TransactionRecord> findByCustomerId(Long customerId) {
        return transactionRecordRepository.findByCustomerId(customerId);
    }

    public Page<TransactionRecord> findByCustomerId(Long customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return transactionRecordRepository.findByCustomerId(customerId, pageable);
    }

    public List<TransactionRecord> findByOrderId(Long orderId) {
        return transactionRecordRepository.findByOrderId(orderId);
    }
}
