package com.washshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("employee")
public class Employee extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long storeId;

    private Long userId;

    private String employeeNo;

    private String position;

    private String department;

    private LocalDateTime entryDate;

    private String idCard;

    private String emergencyContact;

    private String emergencyPhone;

    private Integer status;
}
