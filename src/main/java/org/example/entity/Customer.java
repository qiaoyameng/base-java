package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.enums.MemberLevel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 50)
    private String nickname;

    @Column(length = 20)
    private String phone;

    private String avatar;

    private Integer gender;

    private LocalDate birthday;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MemberLevel memberLevel = MemberLevel.NORMAL;

    private BigDecimal totalPoints = BigDecimal.ZERO;

    private BigDecimal availablePoints = BigDecimal.ZERO;

    private BigDecimal totalConsumption = BigDecimal.ZERO;

    private Integer orderCount = 0;

    @Column(length = 100)
    private String email;

    private Boolean enabled = true;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;
}
