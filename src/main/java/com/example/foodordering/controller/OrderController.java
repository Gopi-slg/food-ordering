package com.example.foodordering.controller;

import com.example.foodordering.dto.OrderResponse;
import com.example.foodordering.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/orders")
/*@PreAuthorize("@customerAccess.canAccessCustomer(#customerId, authentication)")*/
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping
    @PreAuthorize("@customerAccess.canAccessCustomer(#customerId, authentication)")
    public ResponseEntity<OrderResponse> placeOrder(@PathVariable Long customerId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(customerId));
    }

    @GetMapping
    @PreAuthorize("@customerAccess.canAccessCustomer(#customerId, authentication)")
    public ResponseEntity<List<OrderResponse>> getCustomerOrders(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getCustomerOrders(customerId));
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("@customerAccess.canAccessCustomer(#customerId, authentication)")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long customerId, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(customerId, orderId));
    }

    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("@customerAccess.canAccessCustomer(#customerId, authentication)")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long customerId, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(customerId, orderId));
    }
}