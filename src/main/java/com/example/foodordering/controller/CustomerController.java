
package com.example.foodordering.controller;

import com.example.foodordering.dto.CustomerRequest;
import com.example.foodordering.dto.CustomerResponse;
import com.example.foodordering.service.CustomerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // CREATE
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // createCustomer and getAllCustomers
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CustomerRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerService.createCustomer(request));
    }

    // GET ALL
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // createCustomer and getAllCustomers
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    // GET BY ID
    @GetMapping("/{id}")
    @PreAuthorize("@customerAccess.canAccessCustomer(#id, authentication)") // get/update/delete by id
    public ResponseEntity<CustomerResponse> getCustomerById(
            @PathVariable Long id) {

        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("@customerAccess.canAccessCustomer(#id, authentication)") // get/update/delete by id
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {

        return ResponseEntity.ok(
                customerService.updateCustomer(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("@customerAccess.canAccessCustomer(#id, authentication)") // get/update/delete by id
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable Long id) {

        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
