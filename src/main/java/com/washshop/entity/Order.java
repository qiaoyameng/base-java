package com.washshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("orders")
public class Order extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String orderNo;

    private Long userId;

    private Long storeId;

    private Integer orderType;

    private Integer deliveryType;

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

    private String remark;

    private String cancelReason;

    private LocalDateTime confirmTime;

    private LocalDateTime startWashTime;

    private LocalDateTime finishWashTime;

    private LocalDateTime readyTime;

    private LocalDateTime deliverTime;

    private LocalDateTime completeTime;

    private Integer reminderCount;

    private String voucherNo;
}
