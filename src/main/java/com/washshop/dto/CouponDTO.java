package com.washshop.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponDTO {

    private Long id;

    @NotBlank(message = "优惠券名称不能为空")
    private String couponName;

    @NotBlank(message = "优惠券编码不能为空")
    private String couponCode;

    @NotNull(message = "优惠券类型不能为空")
    private Integer couponType;

    private BigDecimal discountAmount;

    private BigDecimal discountRate;

    private BigDecimal minAmount;

    private BigDecimal maxDiscount;

    @NotNull(message = "总数量不能为空")
    private Integer totalQuantity;

    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    private Integer limitPerUser;

    private String applicableServices;

    private String description;

    @NotNull(message = "状态不能为空")
    private Integer status;
}
