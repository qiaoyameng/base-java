package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "t_order")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    @Column(name = "customer_id", insertable = false, updatable = false)
    private Long customerId;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal payableAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal pointsDeductAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal paidAmount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private DeliveryType deliveryType;

    @Column(length = 500)
    private String deliveryAddress;

    @Column(length = 50)
    private String receiverName;

    @Column(length = 20)
    private String receiverPhone;

    @Column(length = 100)
    private String remark;

    private LocalDateTime paymentTime;

    private LocalDateTime prepareTime;

    private LocalDateTime deliveryTime;

    private LocalDateTime completeTime;

    private LocalDateTime cancelTime;

    @Column(length = 200)
    private String cancelReason;

    private Boolean notifiedPrepare = false;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    public enum PaymentMethod {
        ONLINE("线上支付"),
        STORE("到店付款");

        private String desc;

        PaymentMethod(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum OrderStatus {
        PENDING_PAYMENT("待支付"),
        PENDING_PREPARE("待备货"),
        PENDING_PICKUP("待取货"),
        PENDING_DELIVERY("待发货"),
        DELIVERING("配送中"),
        COMPLETED("已完成"),
        CANCELLED("已取消"),
        REFUNDING("退款中"),
        REFUNDED("已退款");

        private String desc;

        OrderStatus(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum DeliveryType {
        STORE_PICKUP("到店取货"),
        DELIVERY("送货上门");

        private String desc;

        DeliveryType(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }
}
