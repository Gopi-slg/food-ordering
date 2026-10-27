package com.example.foodordering.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 100)
    private String name;
    @NotBlank(message = "Email is important")
    @Email(message = "Enter valid email")
    @Size(max = 100)
    private String email;
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "[0-9]{10}",message = "Mobile number must be 10 numbers")
    private String phno;
    public CustomerRequest(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }
}
