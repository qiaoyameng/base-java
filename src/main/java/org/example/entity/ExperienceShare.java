package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "experience_shares")
public class ExperienceShare extends BaseEntity {

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerAvatar;

    @Column(nullable = false, length = 2000)
    private String content;

    @ElementCollection
    @CollectionTable(name = "experience_images", joinColumns = @JoinColumn(name = "experience_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    @Column(nullable = false)
    private Integer likeCount = 0;

    @Column(nullable = false)
    private Integer commentCount = 0;

    private Boolean featured = false;

    private Integer sortOrder = 0;
}
