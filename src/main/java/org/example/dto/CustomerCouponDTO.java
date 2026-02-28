package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.entity.CustomerCoupon;

import java.time.LocalDateTime;

@Data
@Schema(description = "顾客优惠券DTO")
public class CustomerCouponDTO {

    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "优惠券信息")
    private CouponDTO coupon;

    @Schema(description = "领取时间")
    private LocalDateTime receiveTime;

    @Schema(description = "使用时间")
    private LocalDateTime useTime;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "状态：UNUSED-未使用，USED-已使用，EXPIRED-已过期")
    private CustomerCoupon.Status status;
}
