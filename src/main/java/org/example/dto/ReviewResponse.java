package org.example.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private Long id;
    private Long orderId;
    private String orderNo;
    private Long customerId;
    private String customerName;
    private Integer rating;
    private String comment;
    private String reply;
    private Boolean enabled;
    private LocalDateTime createTime;
}
