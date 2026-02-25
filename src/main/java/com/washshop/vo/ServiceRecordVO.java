package com.washshop.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ServiceRecordVO {

    private Long id;
    private Long orderId;
    private Long orderItemId;
    private Long userId;
    private Long storeId;
    private String storeName;
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
    private LocalDateTime createTime;
}
