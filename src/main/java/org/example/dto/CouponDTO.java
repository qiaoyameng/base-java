package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponDTO {
    @NotBlank(message = "优惠券名称不能为空")
    @Size(max = 50, message = "名称不能超过50字")
    private String name;

    @NotNull(message = "优惠金额不能为空")
    @DecimalMin(value = "0.01", message = "优惠金额必须大于0")
    private BigDecimal discountAmount;

    private BigDecimal minOrderAmount;

    @NotNull(message = "发放数量不能为空")
    @Min(value = 1, message = "发放数量必须大于0")
    private Integer totalQuantity;

    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    private String memberLevel;
}
