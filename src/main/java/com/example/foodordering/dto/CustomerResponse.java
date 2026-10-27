package com.example.foodordering.dto;

import java.time.LocalDateTime;

public class CustomerResponse {
    private Long id;
    private String name;
    private String email;
    private String phno;
    private LocalDateTime createdAt;

    public CustomerResponse(){}
    public CustomerResponse(Long id, String name, String email, String phno, LocalDateTime createdAt){
        this.id=id;
        this.name=name;
        this.email=email;
        this.phno=phno;
        this.createdAt=createdAt;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPhno() {
        return phno;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
