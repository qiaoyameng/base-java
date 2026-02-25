package com.washshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("service_record")
public class ServiceRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long orderId;

    private Long orderItemId;

    private Long userId;

    private Long storeId;

    private Long serviceId;

    private String serviceName;

    private LocalDateTime signInTime;

    private LocalDateTime startWashTime;

    private LocalDateTime finishWashTime;

    private LocalDateTime deliverTime;

    private Integer actualDuration;

    private Integer rating;

    private String reviewContent;

    private String reviewImages;

    private LocalDateTime reviewTime;

    private String voucherNo;

    private String voucherContent;
}
