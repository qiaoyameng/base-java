package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;
import org.example.enums.OrderStatus;
import org.example.enums.PaymentMethod;
import org.example.enums.PickupType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String orderNo;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerPhone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PickupType pickupType;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal payAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal pointsDiscount;

    private Long usedPoints;

    private String remark;

    private String address;

    private String trackingNo;

    private LocalDateTime paidTime;

    private LocalDateTime preparedTime;

    private LocalDateTime shippedTime;

    private LocalDateTime completedTime;

    private LocalDateTime cancelledTime;

    private LocalDateTime notificationTime;

    private Boolean notificationSent = false;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();
}
