package com.example.foodordering.service;

import com.example.foodordering.dto.LoginRequest;
import com.example.foodordering.dto.LoginResponse;
import com.example.foodordering.dto.RegisterRequest;
import com.example.foodordering.dto.RegisterResponse;
import com.example.foodordering.entity.Customer;
import com.example.foodordering.entity.Role;
import com.example.foodordering.exception.DuplicateEmailException;
import com.example.foodordering.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.BadCredentialsException;

@Service
public class AuthService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AuthService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService){
        this.customerRepository=customerRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtTokenService=jwtTokenService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request){
        String email=request.email().trim();
        if(customerRepository.existsByEmail(email)){
            throw new DuplicateEmailException("Email was already registered "+email);
        }
        Customer customer=new Customer();
        customer.setName(request.name().trim());
        customer.setEmail(request.email().trim());
        customer.setPassword_hash(passwordEncoder.encode(request.password()));
        customer.setRole(Role.CUSTOMER);
        customer.setPhone(request.phno().trim());

        Customer saved=customerRepository.save(customer);
        return new RegisterResponse(saved.getId(), saved.getName(), saved.getEmail());
    }
    public LoginResponse login(LoginRequest request){
        String  email=request.email().trim().toLowerCase();
        Customer customer=customerRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new org.springframework.security.authentication.
                        BadCredentialsException("Invalid email or password"));
        if(customer.getPassword_hash()==null || !passwordEncoder.matches(request.password(), customer.getPassword_hash())){
            throw new org.springframework.security.authentication
                    .BadCredentialsException("Invalid email or password");
        }

        return new LoginResponse(jwtTokenService.createToken(customer),"Bearer",
                jwtTokenService.getExpirationSeconds(),customer.getId(),customer.getRole().name());

    }
}
