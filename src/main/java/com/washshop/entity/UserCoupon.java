package com.washshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_coupon")
public class UserCoupon extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long couponId;

    private LocalDateTime receiveTime;

    private LocalDateTime useTime;

    private Long orderId;

    private Integer status;
}
