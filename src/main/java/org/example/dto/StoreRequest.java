package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StoreRequest {
    @NotBlank(message = "门店名称不能为空")
    private String name;
    @NotBlank(message = "门店地址不能为空")
    private String address;
    private String phone;
    private String businessHours;
    private String description;
    private Double latitude;
    private Double longitude;
    private Boolean enabled;
}
