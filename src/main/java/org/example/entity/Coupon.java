package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.enums.CouponStatus;
import org.example.enums.CouponType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(length = 100)
    private String description;

    @Column(length = 32)
    private String couponCode;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CouponType type;

    private BigDecimal minAmount;

    private BigDecimal discountValue;

    private Integer totalQuantity;

    private Integer usedCount = 0;

    private Integer limitPerUser = 1;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CouponStatus status = CouponStatus.ACTIVE;

    private Boolean enabled = true;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;
}
