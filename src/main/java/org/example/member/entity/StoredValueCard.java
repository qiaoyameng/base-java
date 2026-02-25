package org.example.member.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stored_value_card")
public class StoredValueCard extends BaseEntity {
    
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    
    @Column(name = "card_no", unique = true, length = 50)
    private String cardNo;
    
    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;
    
    @Column(name = "total_recharge", precision = 10, scale = 2)
    private BigDecimal totalRecharge = BigDecimal.ZERO;
    
    @Column(name = "total_consumed", precision = 10, scale = 2)
    private BigDecimal totalConsumed = BigDecimal.ZERO;
    
    @Column(name = "status")
    private Integer status = 1;
    
    @Column(name = "expire_time")
    private LocalDateTime expireTime;
}
