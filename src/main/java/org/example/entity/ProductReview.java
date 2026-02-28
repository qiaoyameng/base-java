package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_product_review")
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    @Column(length = 50)
    private String customerNickname;

    @Column(length = 200)
    private String customerAvatar;

    private Long productId;

    private Long orderId;

    private Long orderItemId;

    private Integer rating;

    @Column(length = 1000)
    private String content;

    @Column(length = 1000)
    private String images;

    @Column(length = 500)
    private String reply;

    private LocalDateTime replyTime;

    private Boolean isAnonymous = false;

    private Boolean isTop = false;

    private Boolean enabled = true;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;
}
