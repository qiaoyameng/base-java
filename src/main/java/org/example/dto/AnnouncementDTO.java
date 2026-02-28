package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.entity.Announcement;

import java.time.LocalDateTime;

@Data
@Schema(description = "公告DTO")
public class AnnouncementDTO {

    @Schema(description = "公告ID")
    private Long id;

    @NotBlank(message = "公告标题不能为空")
    @Schema(description = "公告标题", required = true)
    private String title;

    @NotBlank(message = "公告内容不能为空")
    @Schema(description = "公告内容", required = true)
    private String content;

    @NotNull(message = "公告类型不能为空")
    @Schema(description = "公告类型：ACTIVITY-活动公告，NOTICE-通知公告，PROMOTION-促销信息", required = true)
    private Announcement.Type type;

    @Schema(description = "公告图片")
    private String imageUrl;

    @NotNull(message = "开始时间不能为空")
    @Schema(description = "开始时间", required = true)
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    @Schema(description = "结束时间", required = true)
    private LocalDateTime endTime;

    @Schema(description = "是否启用")
    private Boolean active;

    @Schema(description = "是否首页展示")
    private Boolean displayOnHome;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "浏览次数")
    private Integer viewCount;
}
