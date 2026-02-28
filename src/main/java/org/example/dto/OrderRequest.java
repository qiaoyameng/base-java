package org.example.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest {
    private Long customerId;
    private List<OrderItemRequest> items;
    private String paymentMethod;
    private String deliveryType;
    private String deliveryAddress;
    private String receiverName;
    private String receiverPhone;
    private String remark;
    private Integer usePoints;
    private Long useCouponId;

    @Data
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;
    }
}
