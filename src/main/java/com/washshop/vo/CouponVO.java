package com.washshop.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponVO {

    private Long id;
    private String couponName;
    private String couponCode;
    private Integer couponType;
    private String couponTypeName;
    private BigDecimal discountAmount;
    private BigDecimal discountRate;
    private BigDecimal minAmount;
    private BigDecimal maxDiscount;
    private Integer totalQuantity;
    private Integer remainingQuantity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer limitPerUser;
    private String applicableServices;
    private String description;
    private Integer status;
    private Boolean received;
}
