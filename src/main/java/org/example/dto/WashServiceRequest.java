package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.ServiceCategory;

import java.math.BigDecimal;

@Data
public class WashServiceRequest {
    @NotBlank(message = "服务名称不能为空")
    private String name;
    private String description;
    @NotNull(message = "服务分类不能为空")
    private ServiceCategory category;
    @NotNull(message = "价格不能为空")
    private BigDecimal price;
    @NotNull(message = "预计时长不能为空")
    private Integer durationMinutes;
    private String image;
    private Long storeId;
}
