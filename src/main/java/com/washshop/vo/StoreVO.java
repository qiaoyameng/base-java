package com.washshop.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class StoreVO {

    private Long id;
    private String storeName;
    private String storeCode;
    private String address;
    private String contactPhone;
    private String contactPerson;
    private LocalTime businessStartTime;
    private LocalTime businessEndTime;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String description;
    private String images;
    private Integer status;
    private Integer sortOrder;
}
