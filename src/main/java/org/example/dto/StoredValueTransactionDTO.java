package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.entity.StoredValueTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "储值交易记录DTO")
public class StoredValueTransactionDTO {

    @Schema(description = "交易ID")
    private Long id;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "会员姓名")
    private String memberName;

    @Schema(description = "交易类型：RECHARGE-充值，CONSUME-消费，REFUND-退款")
    private StoredValueTransaction.Type type;

    @Schema(description = "交易金额")
    private BigDecimal amount;

    @Schema(description = "赠送金额")
    private BigDecimal giftAmount;

    @Schema(description = "交易前余额")
    private BigDecimal balanceBefore;

    @Schema(description = "交易后余额")
    private BigDecimal balanceAfter;

    @Schema(description = "交易编号")
    private String transactionNo;

    @Schema(description = "关联订单ID")
    private Long orderId;

    @Schema(description = "关联订单编号")
    private String orderNo;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "交易时间")
    private LocalDateTime transactionTime;
}
