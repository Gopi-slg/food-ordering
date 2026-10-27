package com.example.foodordering.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(@NotBlank @Size(max = 100) String name,
                              @NotBlank @Email @Size(max = 120) String email,
                              @NotBlank @Pattern(regexp = "[0-9]{10}") String phno,
                              @NotBlank @Size(min = 8, max = 16) String password) {}
