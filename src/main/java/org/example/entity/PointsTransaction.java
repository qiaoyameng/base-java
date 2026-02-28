package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "points_transactions")
public class PointsTransaction extends BaseEntity {

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String memberName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private Integer points;

    @Column(nullable = false)
    private Integer balanceBefore;

    @Column(nullable = false)
    private Integer balanceAfter;

    private Long orderId;

    private String orderNo;

    private String description;

    private LocalDateTime transactionTime;

    public enum Type {
        EARN,       // 获得
        DEDUCT,     // 抵扣
        EXCHANGE,   // 兑换
        EXPIRE      // 过期
    }
}
