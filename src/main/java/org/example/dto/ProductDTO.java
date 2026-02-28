package org.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.ProductCategory;
import org.example.enums.ProductStatus;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    @NotBlank(message = "商品名称不能为空")
    private String name;

    @NotNull(message = "商品价格不能为空")
    @Min(value = 0, message = "价格不能为负数")
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    private Integer stock;

    private String specification;

    @NotNull(message = "商品分类不能为空")
    private ProductCategory category;

    private ProductStatus status = ProductStatus.ON_SALE;

    private String description;

    private String imageUrl;
}
