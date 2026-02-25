package com.washshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("points_record")
public class PointsRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Integer points;

    private Integer type;

    private String source;

    private Long sourceId;

    private String description;

    private Integer balance;
}
