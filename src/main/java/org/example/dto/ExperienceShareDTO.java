package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "体验分享DTO")
public class ExperienceShareDTO {

    @Schema(description = "分享ID")
    private Long id;

    @Schema(description = "顾客ID")
    private Long customerId;

    @Schema(description = "顾客姓名")
    private String customerName;

    @Schema(description = "顾客头像")
    private String customerAvatar;

    @NotBlank(message = "分享内容不能为空")
    @Schema(description = "分享内容", required = true)
    private String content;

    @Schema(description = "分享图片列表")
    private List<String> images;

    @Schema(description = "点赞数")
    private Integer likeCount;

    @Schema(description = "评论数")
    private Integer commentCount;

    @Schema(description = "是否精选")
    private Boolean featured;

    @Schema(description = "排序")
    private Integer sortOrder;
}
