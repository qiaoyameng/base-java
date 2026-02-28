package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_photo_wall")
public class PhotoWall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    @Column(length = 50)
    private String customerNickname;

    @Column(length = 200)
    private String customerAvatar;

    private Long productId;

    @Column(length = 200)
    private String productName;

    @Column(nullable = false, length = 500)
    private String image;

    @Column(length = 200)
    private String description;

    private Integer likes = 0;

    private Boolean isTop = false;

    private Boolean enabled = true;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;
}
