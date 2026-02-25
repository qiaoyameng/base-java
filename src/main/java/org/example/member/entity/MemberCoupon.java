package org.example.member.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;
import org.example.common.enums.CouponStatus;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "member_coupon")
public class MemberCoupon extends BaseEntity {
    
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    
    @Column(name = "coupon_id", nullable = false)
    private Long couponId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CouponStatus status = CouponStatus.AVAILABLE;
    
    @Column(name = "receive_time")
    private LocalDateTime receiveTime;
    
    @Column(name = "use_time")
    private LocalDateTime useTime;
    
    @Column(name = "order_id")
    private Long orderId;
    
    @Column(name = "expire_time")
    private LocalDateTime expireTime;
}
