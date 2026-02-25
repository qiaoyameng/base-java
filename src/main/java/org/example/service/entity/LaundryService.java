package org.example.service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;
import org.example.common.enums.ServiceCategory;
import org.example.common.enums.ServiceStatus;

import java.math.BigDecimal;
import java.time.Duration;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "laundry_service")
public class LaundryService extends BaseEntity {
    
    @Column(name = "service_name", nullable = false, length = 100)
    private String serviceName;
    
    @Column(name = "service_code", unique = true, length = 50)
    private String serviceCode;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ServiceCategory category;
    
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "estimated_duration")
    private Integer estimatedDuration;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ServiceStatus status = ServiceStatus.PENDING;
    
    @Column(name = "store_id")
    private Long storeId;
    
    @Column(name = "image_url", length = 255)
    private String imageUrl;
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    @Column(name = "active")
    private Boolean active = true;
}
