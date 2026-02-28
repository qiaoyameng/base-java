package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "商品规格DTO")
public class ProductSpecificationDTO {

    @Schema(description = "规格ID")
    private Long id;

    @NotBlank(message = "规格名称不能为空")
    @Schema(description = "规格名称", required = true)
    private String name;

    @NotBlank(message = "规格值不能为空")
    @Schema(description = "规格值", required = true)
    private String value;

    @Schema(description = "价格调整")
    private BigDecimal priceAdjustment;

    @NotNull(message = "规格库存不能为空")
    @Schema(description = "规格库存", required = true)
    private Integer stock;
}
