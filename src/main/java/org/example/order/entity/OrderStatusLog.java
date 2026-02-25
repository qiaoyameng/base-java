package org.example.order.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "order_status_log")
public class OrderStatusLog extends BaseEntity {
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "from_status", length = 30)
    private String fromStatus;
    
    @Column(name = "to_status", length = 30)
    private String toStatus;
    
    @Column(name = "operator_id")
    private Long operatorId;
    
    @Column(name = "operator_type", length = 20)
    private String operatorType;
    
    @Column(name = "remark", length = 255)
    private String remark;
    
    @Column(name = "operate_time")
    private LocalDateTime operateTime;
}
