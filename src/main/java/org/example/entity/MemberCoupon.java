package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "member_coupon")
public class MemberCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long couponId;

    @Column(nullable = false, length = 32)
    private String couponCode;

    @Column(nullable = false)
    private Boolean used = false;

    @Column
    private Long orderId;

    @Column
    private LocalDateTime usedTime;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTime;
}
