package org.example.member.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;
import org.example.common.enums.CouponType;
import org.example.common.enums.CouponStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "coupon")
public class Coupon extends BaseEntity {
    
    @Column(name = "coupon_name", nullable = false, length = 100)
    private String couponName;
    
    @Column(name = "coupon_code", unique = true, length = 50)
    private String couponCode;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_type", nullable = false)
    private CouponType couponType;
    
    @Column(name = "discount_value", precision = 10, scale = 2)
    private BigDecimal discountValue;
    
    @Column(name = "min_amount", precision = 10, scale = 2)
    private BigDecimal minAmount = BigDecimal.ZERO;
    
    @Column(name = "max_discount", precision = 10, scale = 2)
    private BigDecimal maxDiscount;
    
    @Column(name = "total_quantity")
    private Integer totalQuantity;
    
    @Column(name = "used_quantity")
    private Integer usedQuantity = 0;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "valid_days")
    private Integer validDays;
    
    @Column(name = "description", length = 255)
    private String description;
    
    @Column(name = "active")
    private Boolean active = true;
}
