package com.example.foodordering.service;

import com.example.foodordering.dto.CustomerRequest;
import com.example.foodordering.dto.CustomerResponse;
import com.example.foodordering.entity.Customer;
import com.example.foodordering.exception.CustomerNotFoundException;
import com.example.foodordering.exception.DuplicateEmailException;
import com.example.foodordering.repository.CustomerRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository=customerRepository;
    }

    public CustomerResponse createCustomer(CustomerRequest request){
        String email=request.getEmail().trim();
        if(customerRepository.existsByEmail(email)){
            throw new DuplicateEmailException("Email is already registered "+email);
        }
        Customer customer=new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhno());

        return toResponse(customerRepository.save(customer));
    }
    @Transactional(readOnly= true)
    public List<CustomerResponse> getAllCustomers(){
        return customerRepository.findAll().stream().map(this::toResponse).toList();
    }
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id){
        return toResponse(findCustomer(id));
    }
    public Customer findCustomer(Long id){
        return customerRepository.findById(id).orElseThrow(()->new CustomerNotFoundException("Customer not found id "+id));
    }
    public CustomerResponse updateCustomer(Long id, CustomerRequest request){
        String email=request.getEmail().trim();
        Customer customer=findCustomer(id);
        if(customerRepository.existsByEmailAndIdNot(email,id)){
            throw new DuplicateEmailException("Email is already registered "+email+" not with id"+id);
        }
        customer.setName(request.getName());
        customer.setEmail(email);
        customer.setPhone(request.getPhno());
        return toResponse(customerRepository.save(customer));
    }
    public void deleteCustomer(Long id){
        customerRepository.delete(findCustomer(id));
        }
    public CustomerResponse toResponse(Customer customer){
        return new CustomerResponse(customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCreatedAt());
    }
}
