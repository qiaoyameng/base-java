package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.enums.ServiceCategory;
import org.example.enums.ServiceStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "wash_services")
public class WashService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private ServiceCategory category;

    private BigDecimal price;

    private Integer durationMinutes;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ServiceStatus status = ServiceStatus.PENDING;

    private Boolean enabled = true;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;
}
