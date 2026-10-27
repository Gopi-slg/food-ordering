package com.example.foodordering.repository;

import com.example.foodordering.entity.Payment;
import com.example.foodordering.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrderIdOrderByCreatedAtDesc(Long orderId);
    boolean existsByOrderIdAndStatus(Long orderId, PaymentStatus status);
    boolean existsByIdAndOrderCustomerId(Long paymentId, Long customerId);
}
