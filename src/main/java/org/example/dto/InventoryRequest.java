package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryRequest {
    @NotNull(message = "门店ID不能为空")
    private Long storeId;
    @NotBlank(message = "物料名称不能为空")
    private String name;
    private String category;
    private String unit;
    private Integer quantity;
    private Integer minStock;
    private String description;
}
