package com.example.foodordering.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(Long cartId,
                           Long customerId,
                           List<CartItemResponse> items,
                           BigDecimal total) {
    public record CartItemResponse(Long cartItemId,
                                   Long foodItemId,
                                   String foodName,
                                   int quantity,
                                   BigDecimal unitprice,
                                   BigDecimal subtotal){
    }
}
