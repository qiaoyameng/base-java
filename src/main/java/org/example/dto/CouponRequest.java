package org.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.CouponType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponRequest {
    @NotNull(message = "优惠券名称不能为空")
    private String name;
    @NotNull(message = "优惠券类型不能为空")
    private CouponType type;
    @NotNull(message = "优惠值不能为空")
    private BigDecimal discountValue;
    private BigDecimal minAmount;
    private BigDecimal maxDiscount;
    private Integer quantity;
    private Integer perLimit;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Long storeId;
    private String description;
}
