package com.example.foodordering.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddToCartRequest(@NotNull(message = "Food item id is required") Long foodItemId,
                               @NotNull(message = "Quantity is required")
                               @Positive(message = "Quantity must be greater than 0")
                               Integer quantity) {

}
