package org.example.dto;

import lombok.Data;
import org.example.enums.ServiceCategory;
import org.example.enums.ServiceStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WashServiceResponse {
    private Long id;
    private String name;
    private String description;
    private ServiceCategory category;
    private BigDecimal price;
    private Integer durationMinutes;
    private String image;
    private Long storeId;
    private String storeName;
    private ServiceStatus status;
    private Boolean enabled;
    private LocalDateTime createTime;
}
