package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer_coupons")
public class CustomerCoupon extends BaseEntity {

    @Column(nullable = false)
    private Long customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(nullable = false)
    private LocalDateTime receiveTime;

    private LocalDateTime useTime;

    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.UNUSED;

    public enum Status {
        UNUSED,     // 未使用
        USED,       // 已使用
        EXPIRED     // 已过期
    }
}
