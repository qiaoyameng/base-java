package com.washshop.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PointsRecordVO {

    private Long id;
    private Integer points;
    private String type;
    private String source;
    private String description;
    private Integer balance;
    private LocalDateTime createTime;
}
