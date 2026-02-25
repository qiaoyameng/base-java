package com.washshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("store_inventory")
public class StoreInventory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long storeId;

    private String itemName;

    private String itemCode;

    private Integer itemType;

    private String unit;

    private Integer stock;

    private Integer minStock;

    private Integer maxStock;

    private BigDecimal unitPrice;

    private String supplier;

    private String description;

    private Integer status;
}
