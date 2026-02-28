package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.entity.Coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "优惠券DTO")
public class CouponDTO {

    @Schema(description = "优惠券ID")
    private Long id;

    @NotBlank(message = "优惠券名称不能为空")
    @Schema(description = "优惠券名称", required = true)
    private String name;

    @Schema(description = "优惠券描述")
    private String description;

    @NotNull(message = "优惠券类型不能为空")
    @Schema(description = "优惠券类型：FIXED_AMOUNT-固定金额，PERCENTAGE-百分比折扣，FREE_SHIPPING-免邮", required = true)
    private Coupon.CouponType type;

    @Schema(description = "优惠金额（固定金额类型）")
    private BigDecimal discountAmount;

    @Schema(description = "折扣百分比（百分比类型）")
    private BigDecimal discountPercent;

    @Schema(description = "最低订单金额")
    private BigDecimal minOrderAmount;

    @Schema(description = "最大优惠金额")
    private BigDecimal maxDiscountAmount;

    @NotNull(message = "发放数量不能为空")
    @Schema(description = "发放数量", required = true)
    private Integer totalQuantity;

    @Schema(description = "剩余数量")
    private Integer remainingQuantity;

    @Schema(description = "每人限领数量")
    private Integer limitPerUser;

    @NotNull(message = "开始时间不能为空")
    @Schema(description = "开始时间", required = true)
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    @Schema(description = "结束时间", required = true)
    private LocalDateTime endTime;

    @Schema(description = "是否启用")
    private Boolean active;

    @Schema(description = "是否首页展示")
    private Boolean displayOnHome;

    @Schema(description = "排序")
    private Integer sortOrder;
}
