package com.example.foodordering.service;

import com.example.foodordering.dto.PaymentRequest;
import com.example.foodordering.entity.*;
import com.example.foodordering.exception.InvalidPaymentOperationException;
import com.example.foodordering.exception.OrderNotFoundException;
import com.example.foodordering.repository.CustomerOrderRepository;
import com.example.foodordering.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock CustomerOrderRepository orderRepository;
    @Mock PaymentRepository paymentRepository;
    @InjectMocks PaymentService paymentService;
    private CustomerOrder order;

    @BeforeEach
    void setUp() {
        order = mock(CustomerOrder.class);
        when(orderRepository.findById(42L)).thenReturn(Optional.of(order));
    }

    private void allowPayment() {
        when(order.getId()).thenReturn(42L);
        when(order.getTotal()).thenReturn(new BigDecimal("250.00"));
        when(orderRepository.existsByIdAndStatus(eq(42L), any())).thenReturn(false);
        when(paymentRepository.existsByOrderIdAndStatus(eq(42L), any())).thenReturn(false);
        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void upiPaymentIsSuccessful() {
        allowPayment();
        var response = paymentService.createPayment(
                42L, new PaymentRequest(PaymentMethod.UPI));

        assertEquals("SUCCESS", response.status());
        assertEquals(new BigDecimal("250.00"), response.amount());

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(captor.capture());
        assertEquals(PaymentStatus.SUCCESS, captor.getValue().getStatus());
    }

    @Test
    void cashOnDeliveryPaymentIsPending() {
        allowPayment();
        var response = paymentService.createPayment(
                42L, new PaymentRequest(PaymentMethod.CASH_ON_DELIVERY));

        assertEquals("PENDING", response.status());

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(captor.capture());
        assertEquals(PaymentStatus.PENDING, captor.getValue().getStatus());
    }

    @Test
    void missingOrderIsRejected() {
        when(orderRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> paymentService.createPayment(
                42L, new PaymentRequest(PaymentMethod.UPI)));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void cancelledOrderIsRejected() {
        when(orderRepository.existsByIdAndStatus(42L, OrderStatus.CANCELLED)).thenReturn(true);

        assertThrows(InvalidPaymentOperationException.class, () -> paymentService.createPayment(
                42L, new PaymentRequest(PaymentMethod.UPI)));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void successfulPaymentCannotBeDuplicated() {
        when(orderRepository.existsByIdAndStatus(42L, OrderStatus.CANCELLED)).thenReturn(false);
        when(paymentRepository.existsByOrderIdAndStatus(42L, PaymentStatus.SUCCESS)).thenReturn(true);

        assertThrows(InvalidPaymentOperationException.class, () -> paymentService.createPayment(
                42L, new PaymentRequest(PaymentMethod.UPI)));
        verify(paymentRepository, never()).save(any(Payment.class));
    }
}
