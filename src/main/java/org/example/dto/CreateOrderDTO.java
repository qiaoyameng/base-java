package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.PaymentMethod;
import org.example.enums.PickupType;

import java.util.List;

@Data
@Schema(description = "创建订单DTO")
public class CreateOrderDTO {

    @NotNull(message = "顾客ID不能为空")
    @Schema(description = "顾客ID", required = true)
    private Long customerId;

    @NotBlank(message = "顾客姓名不能为空")
    @Schema(description = "顾客姓名", required = true)
    private String customerName;

    @NotBlank(message = "顾客电话不能为空")
    @Schema(description = "顾客电话", required = true)
    private String customerPhone;

    @NotNull(message = "支付方式不能为空")
    @Schema(description = "支付方式：ONLINE-线上支付，OFFLINE-到店付款", required = true)
    private PaymentMethod paymentMethod;

    @NotNull(message = "取货方式不能为空")
    @Schema(description = "取货方式：SELF_PICKUP-到店自取，DELIVERY-快递配送", required = true)
    private PickupType pickupType;

    @Schema(description = "使用积分")
    private Long usePoints = 0L;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "收货地址（配送时必填）")
    private String address;

    @NotEmpty(message = "订单商品不能为空")
    @Schema(description = "订单商品列表", required = true)
    private List<@Valid OrderItemDTO> items;
}
