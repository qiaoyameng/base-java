package com.washshop.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class WashServiceDTO {

    private Long id;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @NotBlank(message = "服务名称不能为空")
    private String serviceName;

    @NotBlank(message = "服务编码不能为空")
    private String serviceCode;

    private String description;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    private String unit;

    @NotNull(message = "预计时长不能为空")
    private Integer estimatedDuration;

    private String images;

    private String notes;

    private Integer sortOrder;

    @NotNull(message = "状态不能为空")
    private Integer status;
}
