package com.washshop.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class RechargeDTO {

    @NotNull(message = "充值金额不能为空")
    @Min(value = 1, message = "充值金额必须大于0")
    private BigDecimal amount;

    @NotNull(message = "支付方式不能为空")
    private Integer payType;
}
