package org.example.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StoreResponse {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String businessHours;
    private String description;
    private Double latitude;
    private Double longitude;
    private Boolean enabled;
    private LocalDateTime createTime;
}
