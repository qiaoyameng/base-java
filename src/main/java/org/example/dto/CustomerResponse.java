package org.example.dto;

import lombok.Data;
import org.example.enums.MemberLevel;

import java.math.BigDecimal;

@Data
public class CustomerResponse {
    private Long id;
    private Long userId;
    private String nickname;
    private String phone;
    private String email;
    private MemberLevel memberLevel;
    private BigDecimal totalConsumption;
    private BigDecimal totalPoints;
    private BigDecimal availablePoints;
    private Integer orderCount;
    private Boolean enabled;
}
