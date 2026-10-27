package com.example.foodordering.service;

import com.example.foodordering.dto.OrderResponse;
import com.example.foodordering.entity.*;
import com.example.foodordering.exception.CustomerNotFoundException;
import com.example.foodordering.exception.InvalidOrderOperationException;
import com.example.foodordering.exception.OrderNotFoundException;
import com.example.foodordering.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OrderService {
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerOrderRepository orderRepository;

    public OrderService(CustomerRepository customerRepository, CartRepository cartRepository,
                        CartItemRepository cartItemRepository, CustomerOrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    public OrderResponse placeOrder(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
                new CustomerNotFoundException("Customer not found id " + customerId));
        Cart cart = cartRepository.findByCustomerId(customerId).orElseThrow(() ->
                new InvalidOrderOperationException("Cannot place an order because the cart is empty"));
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        if (cartItems.isEmpty()) throw new InvalidOrderOperationException("Cannot place an order because the cart is empty");

        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            FoodItem food = cartItem.getFoodItem();
            if (!Boolean.TRUE.equals(food.getAvailable())) {
                throw new InvalidOrderOperationException("Food item is currently unavailable: " + food.getName());
            }
            BigDecimal unitPrice = food.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            OrderItem item = new OrderItem();
            item.setFoodItem(food);
            item.setFoodName(food.getName());
            item.setUnitPrice(unitPrice);
            item.setQuantity(cartItem.getQuantity());
            item.setSubtotal(subtotal);
            order.addItem(item);
            total = total.add(subtotal);
        }
        order.setTotal(total);
        CustomerOrder saved = orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getCustomerOrders(Long customerId) {
        ensureCustomerExists(customerId);
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long customerId, Long orderId) {
        ensureCustomerExists(customerId);
        return toResponse(findOrder(customerId, orderId));
    }

    public OrderResponse cancelOrder(Long customerId, Long orderId) {
        ensureCustomerExists(customerId);
        CustomerOrder order = findOrder(customerId, orderId);
        if (order.getStatus() != OrderStatus.PLACED) {
            throw new InvalidOrderOperationException("Only orders with PLACED status can be cancelled");
        }
        order.setStatus(OrderStatus.CANCELLED);
        return toResponse(orderRepository.save(order));
    }

    private void ensureCustomerExists(Long customerId) {
        if (!customerRepository.existsById(customerId))
            throw new CustomerNotFoundException("Customer not found id " + customerId);
    }

    private CustomerOrder findOrder(Long customerId, Long orderId) {
        return orderRepository.findByIdAndCustomerId(orderId, customerId).orElseThrow(() ->
                new OrderNotFoundException("Order not found id " + orderId + " for customer " + customerId));
    }

    private OrderResponse toResponse(CustomerOrder order) {
        List<OrderResponse.OrderItemResponse> items = order.getItems().stream()
                .map(item -> new OrderResponse.OrderItemResponse(item.getId(), item.getFoodItem().getId(),
                        item.getFoodName(), item.getQuantity(), item.getUnitPrice(), item.getSubtotal()))
                .toList();
        return new OrderResponse(order.getId(), order.getCustomer().getId(), order.getStatus().name(),
                order.getTotal(), order.getCreatedAt(), items);
    }
}