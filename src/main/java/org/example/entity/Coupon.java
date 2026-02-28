package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "coupons")
public class Coupon extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType type;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount;

    private BigDecimal discountPercent;

    @Column(precision = 10, scale = 2)
    private BigDecimal minOrderAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal maxDiscountAmount;

    @Column(nullable = false)
    private Integer totalQuantity;

    @Column(nullable = false)
    private Integer remainingQuantity;

    @Column(nullable = false)
    private Integer limitPerUser = 1;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    private Boolean active = true;

    private Boolean displayOnHome = false;

    private Integer sortOrder = 0;

    public enum CouponType {
        FIXED_AMOUNT,      // 固定金额
        PERCENTAGE,        // 百分比折扣
        FREE_SHIPPING      // 免邮
    }
}
