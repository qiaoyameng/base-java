package com.washshop.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ServiceCategoryDTO {

    private Long id;

    @NotBlank(message = "分类名称不能为空")
    private String categoryName;

    @NotBlank(message = "分类编码不能为空")
    private String categoryCode;

    private String description;

    private String icon;

    private Integer sortOrder;

    @NotNull(message = "状态不能为空")
    private Integer status;
}
