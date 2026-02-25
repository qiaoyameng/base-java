package com.washshop.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class StoreDTO {

    private Long id;

    @NotBlank(message = "门店名称不能为空")
    private String storeName;

    @NotBlank(message = "门店编码不能为空")
    private String storeCode;

    @NotBlank(message = "地址不能为空")
    private String address;

    @NotBlank(message = "联系电话不能为空")
    private String contactPhone;

    private String contactPerson;

    @NotNull(message = "营业开始时间不能为空")
    private LocalTime businessStartTime;

    @NotNull(message = "营业结束时间不能为空")
    private LocalTime businessEndTime;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String description;

    private String images;

    @NotNull(message = "状态不能为空")
    private Integer status;

    private Integer sortOrder;
}
