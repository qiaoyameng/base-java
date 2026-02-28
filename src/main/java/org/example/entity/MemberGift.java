package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;
import org.example.enums.MemberLevel;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "member_gifts")
public class MemberGift extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberLevel requiredLevel;

    private Integer requiredPoints;

    private Integer stock;

    private String imageUrl;

    private Boolean active = true;

    private Integer sortOrder = 0;
}
