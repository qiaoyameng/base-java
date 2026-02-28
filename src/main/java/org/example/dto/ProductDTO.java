package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.example.enums.ProductCategory;
import org.example.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "商品DTO")
public class ProductDTO {

    @Schema(description = "商品ID")
    private Long id;

    @NotBlank(message = "商品名称不能为空")
    @Schema(description = "商品名称", required = true)
    private String name;

    @Schema(description = "商品描述")
    private String description;

    @NotNull(message = "商品价格不能为空")
    @Positive(message = "商品价格必须大于0")
    @Schema(description = "商品价格", required = true)
    private BigDecimal price;

    @NotNull(message = "商品库存不能为空")
    @PositiveOrZero(message = "商品库存不能为负数")
    @Schema(description = "商品库存", required = true)
    private Integer stock;

    @NotNull(message = "商品分类不能为空")
    @Schema(description = "商品分类", required = true)
    private ProductCategory category;

    @Schema(description = "商品状态")
    private ProductStatus status;

    @Schema(description = "主图")
    private String mainImage;

    @Schema(description = "商品图片列表")
    private List<String> images;

    @Schema(description = "商品规格")
    private List<ProductSpecificationDTO> specifications;
}
