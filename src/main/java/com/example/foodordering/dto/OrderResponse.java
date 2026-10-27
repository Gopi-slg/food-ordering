package com.example.foodordering.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(Long orderId, Long customerId, String status,
                            BigDecimal total, LocalDateTime createdAt,
                            List<OrderItemResponse> items) {
    public record OrderItemResponse(Long orderItemId, Long foodItemId,
                                    String foodName, int quantity,
                                    BigDecimal unitPrice, BigDecimal subtotal) {}
}