package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.enums.ProductCategory;
import org.example.enums.ProductStatus;

import java.math.BigDecimal;

@Data
@Schema(description = "商品查询条件")
public class ProductQueryDTO {

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品分类")
    private ProductCategory category;

    @Schema(description = "商品状态")
    private ProductStatus status;

    @Schema(description = "最低价格")
    private BigDecimal minPrice;

    @Schema(description = "最高价格")
    private BigDecimal maxPrice;

    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页数量", defaultValue = "10")
    private Integer pageSize = 10;
}
