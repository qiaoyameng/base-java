package com.washshop.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderDTO {

    @NotNull(message = "门店ID不能为空")
    private Long storeId;

    @NotNull(message = "取送方式不能为空")
    private Integer deliveryType;

    private String pickupAddress;

    private String deliveryAddress;

    private String pickupTime;

    private String deliveryTime;

    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;

    @NotBlank(message = "联系人电话不能为空")
    private String contactPhone;

    private Long couponId;

    private String remark;

    @NotEmpty(message = "订单项不能为空")
    private List<OrderItemDTO> items;
}
