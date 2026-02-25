package org.example.member.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "recharge_record")
public class RechargeRecord extends BaseEntity {
    
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    
    @Column(name = "card_id")
    private Long cardId;
    
    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "bonus_amount", precision = 10, scale = 2)
    private BigDecimal bonusAmount = BigDecimal.ZERO;
    
    @Column(name = "payment_method", length = 20)
    private String paymentMethod;
    
    @Column(name = "payment_no", length = 100)
    private String paymentNo;
    
    @Column(name = "status")
    private Integer status = 1;
    
    @Column(name = "description", length = 255)
    private String description;
}
