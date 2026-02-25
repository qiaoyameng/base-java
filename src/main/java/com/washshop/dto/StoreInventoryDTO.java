package com.washshop.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class StoreInventoryDTO {

    private Long id;

    @NotNull(message = "门店ID不能为空")
    private Long storeId;

    @NotBlank(message = "物品名称不能为空")
    private String itemName;

    @NotBlank(message = "物品编码不能为空")
    private String itemCode;

    @NotNull(message = "物品类型不能为空")
    private Integer itemType;

    private String unit;

    @NotNull(message = "库存数量不能为空")
    private Integer stock;

    @NotNull(message = "最小库存不能为空")
    private Integer minStock;

    @NotNull(message = "最大库存不能为空")
    private Integer maxStock;

    private BigDecimal unitPrice;

    private String supplier;

    private String description;

    @NotNull(message = "状态不能为空")
    private Integer status;
}
