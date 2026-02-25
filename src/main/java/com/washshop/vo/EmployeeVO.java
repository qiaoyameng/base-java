package com.washshop.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmployeeVO {

    private Long id;
    private Long storeId;
    private String storeName;
    private Long userId;
    private String username;
    private String realName;
    private String employeeNo;
    private String position;
    private String department;
    private LocalDateTime entryDate;
    private String idCard;
    private String emergencyContact;
    private String emergencyPhone;
    private Integer status;
}
