package org.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.PaymentMethod;

import java.util.List;

@Data
public class OrderDTO {
    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @NotNull(message = "支付方式不能为空")
    private PaymentMethod paymentMethod;

    @Min(value = 0, message = "使用积分不能为负数")
    private Integer pointsUsed = 0;

    private String remark;

    @NotNull(message = "订单项不能为空")
    private List<OrderItemDTO> items;
}
