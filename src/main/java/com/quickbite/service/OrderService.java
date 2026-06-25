package com.quickbite.service;

import com.quickbite.dto.PlaceOrderRequest;
import com.quickbite.model.*;
import com.quickbite.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    @Transactional
    public Order placeOrder(PlaceOrderRequest request, String customerEmail) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // Build order items from cart
        List<CartItem> cartItems = cartItemRepository.findByUserId(customer.getId());
        if (cartItems.isEmpty()) throw new RuntimeException("Cart is empty");

        Order order = Order.builder()
                .customer(customer)
                .restaurant(restaurant)
                .deliveryAddress(request.getDeliveryAddress())
                .build();

        List<OrderItem> orderItems = cartItems.stream().map(cart -> {
            return OrderItem.builder()
                    .order(order)
                    .menuItem(cart.getMenuItem())
                    .quantity(cart.getQuantity())
                    .price(cart.getMenuItem().getPrice())
                    .build();
        }).collect(Collectors.toList());

        double total = orderItems.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        order.setOrderItems(orderItems);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        // Clear cart after placing order
        cartItemRepository.deleteByUserId(customer.getId());

        return savedOrder;
    }

    public List<Order> getMyOrders(String customerEmail) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customer.getId());
    }

    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
