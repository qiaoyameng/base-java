package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 32)
    private String couponCode;

    @Column(length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CouponType type;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(precision = 10, scale = 2)
    private BigDecimal minAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal maxDiscount;

    @Column(length = 20)
    private String memberLevelRequired;

    private Integer totalCount;

    private Integer usedCount = 0;

    private Integer limitPerCustomer;

    private LocalDateTime validFrom;

    private LocalDateTime validTo;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CouponStatus status = CouponStatus.ACTIVE;

    @Column(length = 200)
    private String image;

    private Boolean enabled = true;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    public enum CouponType {
        DISCOUNT("折扣券"),
        FIXED_AMOUNT("满减券"),
        GIFT("赠品券");

        private String desc;

        CouponType(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum CouponStatus {
        ACTIVE("有效"),
        USED_UP("已领完"),
        EXPIRED("已过期"),
        INACTIVE("未激活");

        private String desc;

        CouponStatus(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }
}
