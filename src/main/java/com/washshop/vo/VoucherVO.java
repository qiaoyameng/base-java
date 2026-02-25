package com.washshop.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VoucherVO {

    private String voucherNo;
    private String orderNo;
    private String storeName;
    private String contactName;
    private String contactPhone;
    private LocalDateTime completeTime;
    private BigDecimal actualAmount;
    private List<VoucherItemVO> items;
    private String qrCode;
}
