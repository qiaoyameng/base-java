package org.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.DeliveryMethod;

import java.math.BigDecimal;

@Data
public class OrderRequest {
    @NotNull(message = "服务ID不能为空")
    private Long serviceId;
    private Long storeId;
    @NotNull(message = "数量不能为空")
    private Integer quantity;
    @NotNull(message = "取送方式不能为空")
    private DeliveryMethod deliveryMethod;
    private String pickupAddress;
    private String receiverName;
    private String receiverPhone;
    private String clothesDescription;
    private Long couponUsageId;
    private BigDecimal pointsToUse;
    private Long storedCardId;
    private String remark;
}
