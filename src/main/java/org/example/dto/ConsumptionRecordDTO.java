package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "消费记录DTO")
public class ConsumptionRecordDTO {

    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "顾客ID")
    private Long customerId;

    @Schema(description = "顾客姓名")
    private String customerName;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "消费金额")
    private BigDecimal consumptionAmount;

    @Schema(description = "获得积分")
    private Integer earnedPoints;

    @Schema(description = "使用积分")
    private Integer usedPoints;

    @Schema(description = "积分抵扣金额")
    private BigDecimal pointsDiscount;

    @Schema(description = "消费时间")
    private LocalDateTime consumptionTime;

    @Schema(description = "备注")
    private String remark;
}
