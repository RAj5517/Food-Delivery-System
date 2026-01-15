package com.fooddelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private Long restaurantId;
    private String restaurantName;
    private LocalDateTime createdAt;
    private List<CartItemResponse> items;
    private BigDecimal totalAmount;
}

