package com.fooddelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EarningsResponse {
    private BigDecimal totalEarnings;
    private Long totalDeliveries;
    private BigDecimal averageEarningPerDelivery;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}

