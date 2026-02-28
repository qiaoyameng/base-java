package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "receipts")
public class Receipt extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String receiptNo;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private String orderNo;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private String customerName;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal payAmount;

    private LocalDateTime issueTime;

    private String qrCode;

    private String verificationCode;

    private Boolean verified = false;

    private LocalDateTime verifiedTime;
}
