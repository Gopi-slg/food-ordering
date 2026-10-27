package com.example.foodordering.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCartItemRequest(@NotNull(message = "Quantity is required")
                                    @Positive(message = "Quantity must be > 0")
                                    Integer quantity) {}
