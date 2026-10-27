package com.example.foodordering.dto;

import com.example.foodordering.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(@NotNull PaymentMethod method) {}
