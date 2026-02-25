package com.washshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wash_service")
public class WashService extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long categoryId;

    private String serviceName;

    private String serviceCode;

    private String description;

    private BigDecimal price;

    private String unit;

    private Integer estimatedDuration;

    private String images;

    private String notes;

    private Integer sortOrder;

    private Integer status;
}
