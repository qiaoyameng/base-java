package com.washshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("service_category")
public class ServiceCategory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String categoryName;

    private String categoryCode;

    private String description;

    private String icon;

    private Integer sortOrder;

    private Integer status;
}
