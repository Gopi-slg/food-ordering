package com.example.foodordering.repository;

import com.example.foodordering.entity.CustomerOrder;
import com.example.foodordering.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    Optional<CustomerOrder> findByIdAndCustomerId(Long id, Long customerId);
    boolean existsByIdAndStatus(Long id, OrderStatus status);
}
