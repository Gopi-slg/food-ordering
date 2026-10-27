package com.example.foodordering.controller;

import com.example.foodordering.dto.PaymentRequest;
import com.example.foodordering.dto.PaymentResponse;
import com.example.foodordering.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
/*@PreAuthorize("@customerAccess.canAccessOrder(#orderId, authentication)")*/
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) { this.paymentService = paymentService; }

    @PostMapping("/api/orders/{orderId}/payments")
    @PreAuthorize("@customerAccess.canAccessOrder(#orderId, authentication)")
    public ResponseEntity<PaymentResponse> createPayment(@PathVariable Long orderId,
                                                          @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(orderId, request));
    }

    @GetMapping("/api/orders/{orderId}/payments")
    @PreAuthorize("@customerAccess.canAccessOrder(#orderId, authentication)")
    public ResponseEntity<List<PaymentResponse>> getOrderPayments(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getOrderPayments(orderId));
    }

    @GetMapping("/api/payments/{paymentId}")
    /*@PreAuthorize("@customerAccess.canAccessPayment(#paymentId, authentication)")*/
    @PreAuthorize("@customerAccess.canAccessPayment(#paymentId, authentication)")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPayment(paymentId));
    }
}
