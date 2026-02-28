package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_customer_coupon")
public class CustomerCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    private Long couponId;

    @Column(length = 32)
    private String couponCode;

    @Column(length = 100)
    private String couponName;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CustomerCouponStatus status = CustomerCouponStatus.AVAILABLE;

    private LocalDateTime validFrom;

    private LocalDateTime validTo;

    private Long usedOrderId;

    private LocalDateTime usedTime;

    @CreationTimestamp
    private LocalDateTime receiveTime;

    public enum CustomerCouponStatus {
        AVAILABLE("可用"),
        USED("已使用"),
        EXPIRED("已过期");

        private String desc;

        CustomerCouponStatus(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }
}
