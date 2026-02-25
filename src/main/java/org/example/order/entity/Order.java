package org.example.order.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;
import org.example.common.enums.DeliveryType;
import org.example.common.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    
    @Column(name = "order_no", unique = true, nullable = false, length = 50)
    private String orderNo;
    
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    
    @Column(name = "store_id")
    private Long storeId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status = OrderStatus.PENDING_PAYMENT;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type")
    private DeliveryType deliveryType;
    
    @Column(name = "pickup_address", length = 255)
    private String pickupAddress;
    
    @Column(name = "pickup_contact", length = 50)
    private String pickupContact;
    
    @Column(name = "pickup_phone", length = 20)
    private String pickupPhone;
    
    @Column(name = "pickup_time")
    private LocalDateTime pickupTime;
    
    @Column(name = "delivery_address", length = 255)
    private String deliveryAddress;
    
    @Column(name = "delivery_contact", length = 50)
    private String deliveryContact;
    
    @Column(name = "delivery_phone", length = 20)
    private String deliveryPhone;
    
    @Column(name = "delivery_time")
    private LocalDateTime deliveryTime;
    
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @Column(name = "pay_amount", precision = 10, scale = 2)
    private BigDecimal payAmount = BigDecimal.ZERO;
    
    @Column(name = "points_used")
    private Integer pointsUsed = 0;
    
    @Column(name = "points_earned")
    private Integer pointsEarned = 0;
    
    @Column(name = "coupon_id")
    private Long couponId;
    
    @Column(name = "payment_method", length = 20)
    private String paymentMethod;
    
    @Column(name = "payment_no", length = 100)
    private String paymentNo;
    
    @Column(name = "payment_time")
    private LocalDateTime paymentTime;
    
    @Column(name = "accept_time")
    private LocalDateTime acceptTime;
    
    @Column(name = "complete_time")
    private LocalDateTime completeTime;
    
    @Column(name = "cancel_time")
    private LocalDateTime cancelTime;
    
    @Column(name = "cancel_reason", length = 255)
    private String cancelReason;
    
    @Column(name = "remark", length = 500)
    private String remark;
    
    @Column(name = "employee_id")
    private Long employeeId;
}
