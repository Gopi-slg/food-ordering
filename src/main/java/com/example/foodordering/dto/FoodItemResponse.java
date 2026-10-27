package com.example.foodordering.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FoodItemResponse {
    private Long id;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private boolean available;
    private LocalDateTime createdAt;

    public FoodItemResponse(Long id, String name, String description, String category, BigDecimal price, boolean available, LocalDateTime createdAt ){
        this.id=id;
        this.name=name;
        this.description=description;
        this.category=category;
        this.price=price;
        this.available=available;
        this.createdAt=createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
