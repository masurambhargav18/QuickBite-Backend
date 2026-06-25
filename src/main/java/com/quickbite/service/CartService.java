package com.quickbite.service;

import com.quickbite.model.CartItem;
import com.quickbite.model.MenuItem;
import com.quickbite.model.User;
import com.quickbite.repository.CartItemRepository;
import com.quickbite.repository.MenuItemRepository;
import com.quickbite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;

    public List<CartItem> getCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartItemRepository.findByUserId(user.getId());
    }

    public CartItem addToCart(String email, Long menuItemId, int quantity) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        Optional<CartItem> existing = cartItemRepository
                .findByUserIdAndMenuItemId(user.getId(), menuItemId);

        if (existing.isPresent()) {
            // If item already in cart, increase quantity
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        }

        CartItem newItem = CartItem.builder()
                .user(user)
                .menuItem(menuItem)
                .quantity(quantity)
                .build();
        return cartItemRepository.save(newItem);
    }

    public CartItem updateQuantity(String email, Long cartItemId, int quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (quantity <= 0) {
            cartItemRepository.delete(item);
            return null;
        }
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    public void removeItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public void clearCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        cartItemRepository.deleteByUserId(user.getId());
    }
}
