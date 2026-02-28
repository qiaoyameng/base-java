package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.OrderStatus;
import org.example.enums.PaymentMethod;
import org.example.enums.PickupType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "订单DTO")
public class OrderDTO {

    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "顾客ID")
    private Long customerId;

    @Schema(description = "顾客姓名")
    private String customerName;

    @Schema(description = "顾客电话")
    private String customerPhone;

    @Schema(description = "订单状态")
    private OrderStatus status;

    @Schema(description = "支付方式")
    private PaymentMethod paymentMethod;

    @Schema(description = "取货方式")
    private PickupType pickupType;

    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    @Schema(description = "优惠金额")
    private BigDecimal discountAmount;

    @Schema(description = "实付金额")
    private BigDecimal payAmount;

    @Schema(description = "积分抵扣金额")
    private BigDecimal pointsDiscount;

    @Schema(description = "使用积分")
    private Long usedPoints;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "收货地址")
    private String address;

    @Schema(description = "物流单号")
    private String trackingNo;

    @Schema(description = "支付时间")
    private LocalDateTime paidTime;

    @Schema(description = "备货完成时间")
    private LocalDateTime preparedTime;

    @Schema(description = "发货时间")
    private LocalDateTime shippedTime;

    @Schema(description = "完成时间")
    private LocalDateTime completedTime;

    @Schema(description = "订单商品列表")
    private List<OrderItemDTO> items;
}
