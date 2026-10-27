
package com.example.foodordering.controller;

import com.example.foodordering.dto.AddToCartRequest;
import com.example.foodordering.dto.CartResponse;
import com.example.foodordering.dto.UpdateCartItemRequest;
import com.example.foodordering.service.CartService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers/{customerId}/cart")
@PreAuthorize("@customerAccess.canAccessCustomer(#customerId, authentication)")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // ADD ITEM
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(
            @PathVariable Long customerId,
            @Valid @RequestBody AddToCartRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addToCart(customerId, request));
    }

    // VIEW CART
    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(cartService.getCart(customerId));
    }

    // UPDATE ITEM QUANTITY
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateQuantity(
            @PathVariable Long customerId,
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request) {

        return ResponseEntity.ok(
                cartService.updateQuantity(
                        customerId, cartItemId, request));
    }

    // REMOVE ITEM
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> removeItem(
            @PathVariable Long customerId,
            @PathVariable Long cartItemId) {

        return ResponseEntity.ok(
                cartService.removeItem(customerId, cartItemId));
    }

    // CLEAR CART
    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @PathVariable Long customerId) {

        cartService.clearCart(customerId);
        return ResponseEntity.noContent().build();
    }
}
