package org.example.member.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "point_record")
public class PointRecord extends BaseEntity {
    
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    
    @Column(name = "points")
    private Integer points;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "order_id")
    private Long orderId;
    
    @Column(name = "description", length = 255)
    private String description;
    
    @Column(name = "balance_after")
    private Integer balanceAfter;
}
