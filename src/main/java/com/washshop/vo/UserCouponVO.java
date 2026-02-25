package com.washshop.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserCouponVO {

    private Long id;
    private Long couponId;
    private String couponName;
    private Integer couponType;
    private String couponTypeName;
    private String description;
    private LocalDateTime receiveTime;
    private LocalDateTime useTime;
    private Long orderId;
    private Integer status;
    private String statusName;
}
