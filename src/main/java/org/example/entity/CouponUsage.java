package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.enums.CouponStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "coupon_usages")
public class CouponUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(length = 32)
    private String orderNo;

    private BigDecimal orderAmount;

    private BigDecimal discountAmount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CouponStatus status = CouponStatus.ACTIVE;

    private LocalDateTime usedTime;

    private LocalDateTime receiveTime;

    @CreationTimestamp
    private LocalDateTime createTime;
}
