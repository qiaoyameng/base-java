package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "照片墙DTO")
public class PhotoWallDTO {

    @Schema(description = "照片ID")
    private Long id;

    @NotBlank(message = "图片URL不能为空")
    @Schema(description = "图片URL", required = true)
    private String imageUrl;

    @Schema(description = "顾客ID")
    private Long customerId;

    @Schema(description = "顾客姓名")
    private String customerName;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "关联商品ID")
    private Long productId;

    @Schema(description = "关联商品名称")
    private String productName;

    @Schema(description = "点赞数")
    private Integer likeCount;

    @Schema(description = "是否已审核")
    private Boolean approved;

    @Schema(description = "排序")
    private Integer sortOrder;
}
