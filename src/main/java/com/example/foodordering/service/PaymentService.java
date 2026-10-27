package com.example.foodordering.service;

import com.example.foodordering.dto.PaymentRequest;
import com.example.foodordering.dto.PaymentResponse;
import com.example.foodordering.entity.CustomerOrder;
import com.example.foodordering.entity.Payment;
import com.example.foodordering.entity.PaymentMethod;
import com.example.foodordering.entity.PaymentStatus;
import com.example.foodordering.exception.InvalidPaymentOperationException;
import com.example.foodordering.exception.OrderNotFoundException;
import com.example.foodordering.exception.PaymentNotFoundException;
import com.example.foodordering.repository.CustomerOrderRepository;
import com.example.foodordering.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class PaymentService {
    private final CustomerOrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public PaymentService(CustomerOrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponse createPayment(Long orderId, PaymentRequest request) {
        CustomerOrder order = orderRepository.findById(orderId).orElseThrow(() ->
                new OrderNotFoundException("Order not found id " + orderId));
        if (orderRepository.existsByIdAndStatus(orderId, com.example.foodordering.entity.OrderStatus.CANCELLED))
            throw new InvalidPaymentOperationException("Cannot pay for a cancelled order");
        if (paymentRepository.existsByOrderIdAndStatus(orderId, PaymentStatus.SUCCESS))
            throw new InvalidPaymentOperationException("Order already has a successful payment");

        PaymentStatus status = request.method() == PaymentMethod.CASH_ON_DELIVERY
                ? PaymentStatus.PENDING : PaymentStatus.SUCCESS;
        Payment payment = paymentRepository.save(new Payment(order, request.method(), status, order.getTotal()));
        return toResponse(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getOrderPayments(Long orderId) {
        if (!orderRepository.existsById(orderId)) throw new OrderNotFoundException("Order not found id " + orderId);
        return paymentRepository.findByOrderIdOrderByCreatedAtDesc(orderId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId).map(this::toResponse).orElseThrow(() ->
                new PaymentNotFoundException("Payment not found id " + paymentId));
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(payment.getId(), payment.getOrder().getId(), payment.getMethod().name(),
                payment.getStatus().name(), payment.getAmount(), payment.getCreatedAt());
    }
}
