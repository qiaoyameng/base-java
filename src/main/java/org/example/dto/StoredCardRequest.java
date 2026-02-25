package org.example.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StoredCardRequest {
    private Long customerId;
    private String cardNumber;
    private BigDecimal balance;
    private String description;
}
