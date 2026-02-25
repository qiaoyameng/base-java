package com.washshop.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVO {

    private Long id;
    private String orderNo;
    private Long userId;
    private Long storeId;
    private String storeName;
    private Integer orderType;
    private Integer deliveryType;
    private String deliveryTypeName;
    private String pickupAddress;
    private String deliveryAddress;
    private String pickupTime;
    private String deliveryTime;
    private String contactName;
    private String contactPhone;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal couponAmount;
    private BigDecimal deliveryFee;
    private BigDecimal actualAmount;
    private Integer payType;
    private LocalDateTime payTime;
    private String payNo;
    private Integer orderStatus;
    private String orderStatusName;
    private String remark;
    private String cancelReason;
    private LocalDateTime createTime;
    private LocalDateTime confirmTime;
    private LocalDateTime startWashTime;
    private LocalDateTime finishWashTime;
    private LocalDateTime readyTime;
    private LocalDateTime deliverTime;
    private LocalDateTime completeTime;
    private String voucherNo;
    private List<OrderItemVO> items;
}
