package com.washshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("recharge_record")
public class RechargeRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private String rechargeNo;

    private BigDecimal amount;

    private BigDecimal giftAmount;

    private Integer payType;

    private String payNo;

    private LocalDateTime payTime;

    private Integer status;
}
