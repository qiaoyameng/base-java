package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;
import org.example.enums.MemberLevel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "members")
public class Member extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    private String email;

    private String avatar;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberLevel level = MemberLevel.NORMAL;

    @Column(nullable = false)
    private Integer points = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalConsumption;

    @Column(precision = 10, scale = 2)
    private BigDecimal storedValueBalance;

    private LocalDate birthday;

    private String address;

    private LocalDateTime lastLoginTime;

    private Integer loginCount = 0;

    private Boolean active = true;
}
