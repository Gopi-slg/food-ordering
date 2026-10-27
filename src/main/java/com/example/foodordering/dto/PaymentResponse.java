package com.example.foodordering.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(Long paymentId, Long orderId, String method, String status,
                              BigDecimal amount, LocalDateTime createdAt) {}
