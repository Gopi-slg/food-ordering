package com.example.foodordering.service;

import com.example.foodordering.dto.AddToCartRequest;
import com.example.foodordering.dto.CartResponse;
import com.example.foodordering.dto.UpdateCartItemRequest;
import com.example.foodordering.entity.Cart;
import com.example.foodordering.entity.CartItem;
import com.example.foodordering.entity.Customer;
import com.example.foodordering.entity.FoodItem;
import com.example.foodordering.exception.CartItemNotFoundException;
import com.example.foodordering.exception.CustomerNotFoundException;
import com.example.foodordering.exception.FoodNotFoundException;
import com.example.foodordering.exception.InvalidCartOperationException;
import com.example.foodordering.repository.CartItemRepository;
import com.example.foodordering.repository.CartRepository;
import com.example.foodordering.repository.CustomerRepository;
import com.example.foodordering.repository.FoodItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final FoodItemRepository foodItemRepository;
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       CustomerRepository customerRepository, FoodItemRepository foodItemRepository){
        this.cartRepository=cartRepository;
        this.cartItemRepository=cartItemRepository;
        this.customerRepository=customerRepository;
        this.foodItemRepository=foodItemRepository;
    }
    public CartResponse addToCart(Long customerId, AddToCartRequest request){
        Customer customer=customerRepository.findById(customerId).orElseThrow(() ->
                new CustomerNotFoundException("Customer not found id "+customerId));
        FoodItem foodItem=foodItemRepository.findById(request.foodItemId()).orElseThrow(() ->
                new FoodNotFoundException("Food not found id "+request.foodItemId()));
        if(!foodItem.getAvailable()){
            throw new InvalidCartOperationException("Food is currently unavailable!!!!");
        }
        Cart cart=cartRepository.findByCustomerId(customerId).orElseGet(() ->{
            Cart newcart=new Cart();
            newcart.setCustomer(customer);
            return cartRepository.save(newcart);
        });

        CartItem cartItem=cartItemRepository.findByCartIdAndFoodItemId(cart.getId(), foodItem.getId())
                .orElseGet(() -> {
                    CartItem newItem=new CartItem();
                    newItem.setCart(cart);
                    newItem.setFoodItem(foodItem);
                    newItem.setQuantity(0);
                    return newItem;
                });
        cartItem.setQuantity(cartItem.getQuantity()+request.quantity());
        cartItemRepository.save(cartItem);
        return buildCartResponse(cart);
    }
    @Transactional(readOnly = true)
    public CartResponse getCart(Long customerId) {

        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() ->
                        new InvalidCartOperationException(
                                "Cart not found for customer ID: "
                                        + customerId));

        return buildCartResponse(cart);
    }

    // UPDATE QUANTITY
    public CartResponse updateQuantity(
            Long customerId,
            Long cartItemId,
            UpdateCartItemRequest request) {

        Cart cart = getExistingCart(customerId);

        CartItem cartItem = cartItemRepository.findByIdAndCartId(cartItemId, cart.getId())
                .orElseThrow(() ->
                        new CartItemNotFoundException("Cart item not found"+cartItemId));

        if (!cartItem.getFoodItem().getAvailable()) {
            throw new InvalidCartOperationException(
                    "Food item is currently unavailable");
        }

        cartItem.setQuantity(request.quantity());
        cartItemRepository.save(cartItem);

        return buildCartResponse(cart);
    }

    // REMOVE ONE ITEM
    public CartResponse removeItem(Long customerId, Long cartItemId) {
        Cart cart = getExistingCart(customerId);

        CartItem cartItem = cartItemRepository.findByIdAndCartId(cartItemId, cart.getId())
                .orElseThrow(() ->
                        new CartItemNotFoundException("Cart item not found with id "+cartItemId));

        cartItemRepository.delete(cartItem);

        return buildCartResponse(cart);
    }

    // CLEAR ENTIRE CART
    public void clearCart(Long customerId) {
        Cart cart = getExistingCart(customerId);

        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());

        cartItemRepository.deleteAll(items);
    }

    private Cart getExistingCart(Long customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(
                        () -> new CustomerNotFoundException("Customer not found id "+customerId));

        return cartRepository.findByCustomerId(customerId)
                .orElseThrow(() ->
                        new InvalidCartOperationException("Cart not found for customer ID: " +customerId));
    }
    private CartResponse buildCartResponse(Cart cart) {
        List<CartResponse.CartItemResponse> items =
                cartItemRepository.findByCartId(cart.getId())
                        .stream()
                        .map(item -> {
                            BigDecimal price =
                                    item.getFoodItem().getPrice();

                            BigDecimal subtotal = price.multiply(
                                    BigDecimal.valueOf(item.getQuantity()));

                            return new CartResponse.CartItemResponse(
                                    item.getId(),
                                    item.getFoodItem().getId(),
                                    item.getFoodItem().getName(),
                                    item.getQuantity(),
                                    price,
                                    subtotal
                            );
                        })
                        .toList();

        BigDecimal total = items.stream()
                .map(CartResponse.CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(
                cart.getId(),
                cart.getCustomer().getId(),
                items,
                total
        );
    }
}
