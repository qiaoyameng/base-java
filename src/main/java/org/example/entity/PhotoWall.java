package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "photo_wall")
public class PhotoWall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column
    private Long productId;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(length = 200)
    private String description;

    @Column(nullable = false)
    private Integer likes = 0;

    @Column(nullable = false)
    private Boolean approved = false;

    @Column(nullable = false)
    private Boolean deleted = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTime;
}
