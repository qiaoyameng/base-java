package org.example.order.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "refund_record")
public class RefundRecord extends BaseEntity {
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    
    @Column(name = "refund_no", unique = true, length = 50)
    private String refundNo;
    
    @Column(name = "refund_amount", precision = 10, scale = 2)
    private BigDecimal refundAmount;
    
    @Column(name = "reason", length = 255)
    private String reason;
    
    @Column(name = "status")
    private Integer status = 0;
    
    @Column(name = "apply_time")
    private java.time.LocalDateTime applyTime;
    
    @Column(name = "audit_time")
    private java.time.LocalDateTime auditTime;
    
    @Column(name = "auditor_id")
    private Long auditorId;
    
    @Column(name = "audit_remark", length = 255)
    private String auditRemark;
    
    @Column(name = "refund_time")
    private java.time.LocalDateTime refundTime;
}
