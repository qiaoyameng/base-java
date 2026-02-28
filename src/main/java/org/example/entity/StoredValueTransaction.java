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
@Table(name = "stored_value_transactions")
public class StoredValueTransaction extends BaseEntity {

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String memberName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(precision = 10, scale = 2)
    private BigDecimal giftAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balanceBefore;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balanceAfter;

    @Column(nullable = false, unique = true)
    private String transactionNo;

    private Long orderId;

    private String orderNo;

    private String remark;

    private LocalDateTime transactionTime;

    public enum Type {
        RECHARGE,   // 充值
        CONSUME,    // 消费
        REFUND      // 退款
    }
}
