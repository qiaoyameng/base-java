package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "评价DTO")
public class ReviewDTO {

    @Schema(description = "评价ID")
    private Long id;

    @Schema(description = "顾客ID")
    private Long customerId;

    @Schema(description = "顾客姓名")
    private String customerName;

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", required = true)
    private Long productId;

    @Schema(description = "商品名称")
    private String productName;

    @NotNull(message = "订单ID不能为空")
    @Schema(description = "订单ID", required = true)
    private Long orderId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    @Schema(description = "评分（1-5星）", required = true)
    private Integer rating;

    @NotBlank(message = "评价内容不能为空")
    @Schema(description = "评价内容", required = true)
    private String content;

    @Schema(description = "评价图片列表")
    private List<String> images;

    @Schema(description = "点赞数")
    private Integer likeCount;

    @Schema(description = "是否已验证购买")
    private Boolean verified;

    @Schema(description = "商家回复")
    private String reply;
}
