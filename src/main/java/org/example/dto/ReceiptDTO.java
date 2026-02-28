package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "电子消费凭证DTO")
public class ReceiptDTO {

    @Schema(description = "凭证ID")
    private Long id;

    @Schema(description = "凭证编号")
    private String receiptNo;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "顾客ID")
    private Long customerId;

    @Schema(description = "顾客姓名")
    private String customerName;

    @Schema(description = "总金额")
    private BigDecimal totalAmount;

    @Schema(description = "优惠金额")
    private BigDecimal discountAmount;

    @Schema(description = "实付金额")
    private BigDecimal payAmount;

    @Schema(description = "开具时间")
    private LocalDateTime issueTime;

    @Schema(description = "二维码")
    private String qrCode;

    @Schema(description = "验证码")
    private String verificationCode;

    @Schema(description = "是否已验证")
    private Boolean verified;

    @Schema(description = "验证时间")
    private LocalDateTime verifiedTime;
}
