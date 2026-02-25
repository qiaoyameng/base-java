package org.example.dto;

import lombok.Data;
import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderResponse {
    private Long id;
    private String orderNo;
    private Long customerId;
    private Long serviceId;
    private String serviceName;
    private Long storeId;
    private String storeName;
    private String clothesDescription;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal actualAmount;
    private BigDecimal pointsDeduction;
    private DeliveryMethod deliveryMethod;
    private String pickupAddress;
    private String receiverName;
    private String receiverPhone;
    private OrderStatus status;
    private LocalDateTime paymentTime;
    private LocalDateTime acceptTime;
    private LocalDateTime startTime;
    private LocalDateTime completedTime;
    private LocalDateTime pickupTime;
    private LocalDateTime estimatedCompleteTime;
    private String remark;
    private LocalDateTime createTime;
}
