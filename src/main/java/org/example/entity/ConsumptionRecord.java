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
@Table(name = "consumption_records")
public class ConsumptionRecord extends BaseEntity {

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false, unique = true)
    private String orderNo;

    @Column(precision = 10, scale = 2)
    private BigDecimal consumptionAmount;

    private Integer earnedPoints;

    private Integer usedPoints;

    @Column(precision = 10, scale = 2)
    private BigDecimal pointsDiscount;

    private LocalDateTime consumptionTime;

    private String remark;
}
