package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class WashOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 32)
    private String orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private WashService service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    private String serviceName;

    private String clothesDescription;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalAmount;

    private BigDecimal discountAmount;

    private BigDecimal actualAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private CouponUsage usedCoupon;

    private BigDecimal pointsUsed = BigDecimal.ZERO;

    private BigDecimal pointsDeduction = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private DeliveryMethod deliveryMethod;

    private String pickupAddress;

    private String receiverName;

    private String receiverPhone;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private OrderStatus status = OrderStatus.PENDING_PAYMENT;

    private LocalDateTime paymentTime;

    private LocalDateTime acceptTime;

    private LocalDateTime startTime;

    private LocalDateTime completedTime;

    private LocalDateTime pickupTime;

    private LocalDateTime estimatedCompleteTime;

    private String remark;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;
}
