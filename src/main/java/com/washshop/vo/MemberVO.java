package com.washshop.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MemberVO {

    private Long id;
    private Long userId;
    private Integer level;
    private String levelName;
    private Integer points;
    private BigDecimal balance;
    private BigDecimal totalConsumption;
    private Integer totalOrders;
    private LocalDateTime upgradeTime;
    private LocalDateTime expireTime;
}
