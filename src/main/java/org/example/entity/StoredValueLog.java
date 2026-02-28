package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_stored_value_log")
public class StoredValueLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    @Column(length = 50)
    private String cardNo;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private LogType type;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(precision = 10, scale = 2)
    private BigDecimal balanceBefore;

    @Column(precision = 10, scale = 2)
    private BigDecimal balanceAfter;

    @Column(length = 200)
    private String description;

    private Long relatedOrderId;

    @Column(length = 32)
    private String relatedOrderNo;

    @CreationTimestamp
    private LocalDateTime createTime;

    public enum LogType {
        RECHARGE("充值"),
        CONSUME("消费"),
        REFUND("退款"),
        GIFT("赠送");

        private String desc;

        LogType(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }
}
