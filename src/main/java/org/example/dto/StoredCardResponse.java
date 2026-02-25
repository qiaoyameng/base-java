package org.example.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StoredCardResponse {
    private Long id;
    private Long customerId;
    private String cardNumber;
    private BigDecimal balance;
    private Boolean enabled;
    private LocalDateTime createTime;
}
