package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "points_transactions")
public class PointsTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(length = 32)
    private String orderNo;

    @Column(length = 20)
    private String type;

    private BigDecimal points;

    private BigDecimal pointsBefore;

    private BigDecimal pointsAfter;

    @Column(length = 100)
    private String description;

    @CreationTimestamp
    private LocalDateTime createTime;
}
