package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_receipt")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 32)
    private String receiptNo;

    private Long orderId;

    @Column(length = 32)
    private String orderNo;

    private Long customerId;

    @Column(length = 50)
    private String customerNickname;

    @Column(length = 20)
    private String customerPhone;

    @Column(columnDefinition = "TEXT")
    private String items;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal paidAmount;

    @Column(length = 100)
    private String paymentMethodName;

    @Column(length = 500)
    private String remark;

    @Column(columnDefinition = "TEXT")
    private String qrCode;

    @CreationTimestamp
    private LocalDateTime createTime;

    private LocalDateTime paidTime;
}
