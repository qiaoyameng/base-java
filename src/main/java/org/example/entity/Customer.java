package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50)
    private String username;

    @Column(length = 100)
    private String password;

    @Column(length = 50)
    private String nickname;

    @Column(length = 11)
    private String phone;

    @Column(length = 200)
    private String avatar;

    @Column(length = 500)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MemberLevel memberLevel = MemberLevel.NORMAL;

    private Integer totalPoints = 0;

    private Integer availablePoints = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal storedValue = BigDecimal.ZERO;

    @Column(length = 50)
    private String storedValueCardNo;

    private Boolean enabled = true;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    public enum MemberLevel {
        NORMAL("普通会员", 0, "0", 0),
        SILVER("银卡会员", 1, "0.95", 5),
        GOLD("金卡会员", 2, "0.88", 10);

        private String desc;
        private Integer level;
        private String discount;
        private Integer pointsRate;

        MemberLevel(String desc, Integer level, String discount, Integer pointsRate) {
            this.desc = desc;
            this.level = level;
            this.discount = discount;
            this.pointsRate = pointsRate;
        }

        public String getDesc() {
            return desc;
        }

        public Integer getLevel() {
            return level;
        }

        public BigDecimal getDiscount() {
            return new BigDecimal(discount);
        }

        public Integer getPointsRate() {
            return pointsRate;
        }
    }
}
