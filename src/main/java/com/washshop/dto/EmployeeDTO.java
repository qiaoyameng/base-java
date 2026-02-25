package com.washshop.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EmployeeDTO {

    private Long id;

    @NotNull(message = "门店ID不能为空")
    private Long storeId;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "员工编号不能为空")
    private String employeeNo;

    @NotBlank(message = "职位不能为空")
    private String position;

    private String department;

    @NotNull(message = "入职日期不能为空")
    private LocalDateTime entryDate;

    private String idCard;

    private String emergencyContact;

    private String emergencyPhone;

    @NotNull(message = "状态不能为空")
    private Integer status;
}
