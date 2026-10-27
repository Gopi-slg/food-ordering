package com.example.foodordering.repository;

import com.example.foodordering.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId);

    Optional<CartItem> findByCartIdAndFoodItemId(Long cartId, Long foodItemId);

    Optional<CartItem> findByIdAndCartId(Long cartItemId, Long cartId);
}
