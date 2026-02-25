package com.washshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("store")
public class Store extends BaseEntity {

    private static final long serialVersionUID = 1L;

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
