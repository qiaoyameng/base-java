package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.enums.OrderStatus;
import org.example.enums.PaymentMethod;

import java.time.LocalDateTime;

@Data
@Schema(description = "订单查询条件")
public class OrderQueryDTO {

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "顾客ID")
    private Long customerId;

    @Schema(description = "顾客姓名")
    private String customerName;

    @Schema(description = "订单状态")
    private OrderStatus status;

    @Schema(description = "支付方式")
    private PaymentMethod paymentMethod;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页数量", defaultValue = "10")
    private Integer pageSize = 10;
}
