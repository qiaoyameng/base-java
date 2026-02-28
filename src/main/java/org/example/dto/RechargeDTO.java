package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "储值充值DTO")
public class RechargeDTO {

    @NotNull(message = "会员ID不能为空")
    @Schema(description = "会员ID", required = true)
    private Long memberId;

    @NotNull(message = "充值金额不能为空")
    @Min(value = 1, message = "充值金额必须大于0")
    @Schema(description = "充值金额", required = true)
    private BigDecimal amount;

    @Schema(description = "赠送金额")
    private BigDecimal giftAmount;

    @Schema(description = "备注")
    private String remark;
}
