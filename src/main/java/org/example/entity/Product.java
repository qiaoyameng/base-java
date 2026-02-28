package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    private BigDecimal originalPrice;

    private Integer stock;

    @Column(length = 50)
    private String spec;

    @Column(length = 1000)
    private String images;

    @Column(length = 500)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;

    @Column(name = "category_id", insertable = false, updatable = false)
    private Long categoryId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ProductStatus status;

    private Integer sortOrder;

    private Integer salesCount = 0;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    public enum ProductStatus {
        ON_SALE("在售"),
        OFF_SALE("下架"),
        OUT_OF_STOCK("缺货"),
        PRE_SALE("预售");

        private String desc;

        ProductStatus(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }
}
