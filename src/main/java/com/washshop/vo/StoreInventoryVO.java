package com.washshop.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreInventoryVO {

    private Long id;
    private Long storeId;
    private String storeName;
    private String itemName;
    private String itemCode;
    private Integer itemType;
    private String itemTypeName;
    private String unit;
    private Integer stock;
    private Integer minStock;
    private Integer maxStock;
    private BigDecimal unitPrice;
    private String supplier;
    private String description;
    private Integer status;
    private Boolean lowStock;
}
