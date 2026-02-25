package com.washshop.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VoucherItemVO {

    private String serviceName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
