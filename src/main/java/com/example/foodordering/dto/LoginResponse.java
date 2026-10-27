package com.example.foodordering.dto;

public record LoginResponse(String accessToken,
                            String tokenType,
                            long expiresIn,
                            Long customerId,
                            String role) {}
