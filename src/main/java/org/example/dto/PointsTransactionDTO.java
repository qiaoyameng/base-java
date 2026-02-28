package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.entity.PointsTransaction;

import java.time.LocalDateTime;

@Data
@Schema(description = "积分交易记录DTO")
public class PointsTransactionDTO {

    @Schema(description = "交易ID")
    private Long id;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "会员姓名")
    private String memberName;

    @Schema(description = "交易类型：EARN-获得，DEDUCT-抵扣，EXCHANGE-兑换，EXPIRE-过期")
    private PointsTransaction.Type type;

    @Schema(description = "积分数量")
    private Integer points;

    @Schema(description = "交易前积分")
    private Integer balanceBefore;

    @Schema(description = "交易后积分")
    private Integer balanceAfter;

    @Schema(description = "关联订单ID")
    private Long orderId;

    @Schema(description = "关联订单编号")
    private String orderNo;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "交易时间")
    private LocalDateTime transactionTime;
}
