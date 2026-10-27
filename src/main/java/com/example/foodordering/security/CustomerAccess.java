package com.example.foodordering.security;

import com.example.foodordering.repository.CustomerOrderRepository;
import com.example.foodordering.repository.CustomerRepository;
import com.example.foodordering.repository.PaymentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("customerAccess")
public class CustomerAccess {
    private final CustomerRepository customerRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final PaymentRepository paymentRepository;
    public CustomerAccess(CustomerRepository customerRepository, CustomerOrderRepository customerOrderRepository
    ,PaymentRepository paymentRepository){
        this.customerOrderRepository=customerOrderRepository;
        this.customerRepository=customerRepository;
        this.paymentRepository=paymentRepository;
    }
    public boolean canAccessCustomer(Long customerId, Authentication auth){
        if(isAdmin(auth)) return true;
        return customerRepository.findByEmailIgnoreCase(auth.getName()).map(customer -> customer.getId()
                .equals(customerId)).orElse(false);
    }
    public boolean canAccessOrder(Long orderId, Authentication auth){
        if(isAdmin(auth)) return true;
        return customerRepository.findByEmailIgnoreCase(auth.getName()).map(customer -> customerOrderRepository
                .findByIdAndCustomerId(orderId, customer.getId()).isPresent()).orElse(false);
    }
    public boolean canAccessPayment(Long paymentId, Authentication auth) {
        if (isAdmin(auth)) return true;

        return customerRepository.findByEmailIgnoreCase(auth.getName())
                .map(customer -> paymentRepository.existsByIdAndOrderCustomerId(
                        paymentId, customer.getId()))
                .orElse(false);
    }
    private boolean isAdmin(Authentication auth){
        return auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority()
                        .equals("ROLE_ADMIN"));
    }
}
