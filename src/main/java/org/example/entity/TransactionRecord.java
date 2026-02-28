package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_transaction_record")
public class TransactionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 32)
    private String transactionNo;

    private Long customerId;

    private Long orderId;

    @Column(length = 32)
    private String orderNo;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TransactionType type;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    private Integer points;

    @Column(length = 200)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentMethod paymentMethod;

    @CreationTimestamp
    private LocalDateTime createTime;

    public enum TransactionType {
        PURCHASE("消费"),
        REFUND("退款"),
        POINTS_EARN("积分获得"),
        POINTS_DEDUCT("积分抵扣"),
        STORED_VALUE_RECHARGE("储值充值"),
        STORED_VALUE_CONSUME("储值消费");

        private String desc;

        TransactionType(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum PaymentMethod {
        WECHAT("微信"),
        ALIPAY("支付宝"),
        CARD("银行卡"),
        STORED_VALUE("储值卡"),
        CASH("现金");

        private String desc;

        PaymentMethod(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }
}
