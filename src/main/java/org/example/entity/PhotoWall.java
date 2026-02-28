package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "photo_wall")
public class PhotoWall extends BaseEntity {

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private String customerName;

    private String description;

    private Long productId;

    private String productName;

    private Integer likeCount = 0;

    private Boolean approved = false;

    private Integer sortOrder = 0;
}
