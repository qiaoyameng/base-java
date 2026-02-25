package org.example.member.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;
import org.example.common.enums.MemberLevel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "member")
public class Member extends BaseEntity {
    
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    
    @Column(name = "member_name", length = 50)
    private String memberName;
    
    @Column(name = "phone", nullable = false, unique = true, length = 20)
    private String phone;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private MemberLevel level = MemberLevel.NORMAL;
    
    @Column(name = "points")
    private Integer points = 0;
    
    @Column(name = "total_points")
    private Integer totalPoints = 0;
    
    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;
    
    @Column(name = "total_spent", precision = 10, scale = 2)
    private BigDecimal totalSpent = BigDecimal.ZERO;
    
    @Column(name = "level_expire_time")
    private LocalDateTime levelExpireTime;
    
    @Column(name = "status")
    private Integer status = 1;
}
