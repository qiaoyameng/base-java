package org.example.dto;

import lombok.Data;
import org.example.enums.CouponType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponResponse {
    private Long id;
    private String name;
    private CouponType type;
    private BigDecimal discountValue;
    private BigDecimal minAmount;
    private BigDecimal maxDiscount;
    private Integer quantity;
    private Integer usedCount;
    private Integer perLimit;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Long storeId;
    private String storeName;
    private Boolean enabled;
    private String description;
}
