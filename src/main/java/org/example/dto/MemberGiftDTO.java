package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.MemberLevel;

@Data
@Schema(description = "会员赠品DTO")
public class MemberGiftDTO {

    @Schema(description = "赠品ID")
    private Long id;

    @NotBlank(message = "赠品名称不能为空")
    @Schema(description = "赠品名称", required = true)
    private String name;

    @Schema(description = "赠品描述")
    private String description;

    @NotNull(message = "所需等级不能为空")
    @Schema(description = "所需会员等级", required = true)
    private MemberLevel requiredLevel;

    @Schema(description = "所需积分")
    private Integer requiredPoints;

    @Schema(description = "库存")
    private Integer stock;

    @Schema(description = "图片")
    private String imageUrl;

    @Schema(description = "是否启用")
    private Boolean active;

    @Schema(description = "排序")
    private Integer sortOrder;
}
