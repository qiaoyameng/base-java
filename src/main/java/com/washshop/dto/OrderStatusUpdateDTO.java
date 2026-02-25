package com.washshop.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderStatusUpdateDTO {

    @NotNull(message = "订单状态不能为空")
    private Integer status;

    private String remark;
}
