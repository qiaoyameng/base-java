package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "stored_card_transactions")
public class StoredCardTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private StoredCard storedCard;

    @Column(length = 32)
    private String transactionNo;

    @Column(length = 32)
    private String orderNo;

    @Column(length = 20)
    private String type;

    private BigDecimal amount;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;

    @Column(length = 100)
    private String description;

    @CreationTimestamp
    private LocalDateTime createTime;
}
