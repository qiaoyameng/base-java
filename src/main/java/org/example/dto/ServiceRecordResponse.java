package org.example.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ServiceRecordResponse {
    private Long id;
    private Long orderId;
    private String orderNo;
    private Long serviceId;
    private String serviceName;
    private Long customerId;
    private String customerName;
    private Long storeId;
    private String storeName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private BigDecimal actualAmount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long washDuration;
    private String operatorName;
    private String details;
}
