package com.washshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("member")
public class Member extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Integer level;

    private Integer points;

    private BigDecimal balance;

    private BigDecimal totalConsumption;

    private Integer totalOrders;

    private LocalDateTime upgradeTime;

    private LocalDateTime expireTime;
}
