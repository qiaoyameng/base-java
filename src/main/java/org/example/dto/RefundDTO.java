package org.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RefundDTO {
    @NotNull(message = "退款金额不能为空")
    private BigDecimal refundAmount;

    private String reason;
}
