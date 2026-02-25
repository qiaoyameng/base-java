package com.washshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("coupon")
public class Coupon extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String couponName;

    private String couponCode;

    private Integer couponType;

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
}
