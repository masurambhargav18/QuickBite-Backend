package com.quickbite.service;

import com.quickbite.model.Restaurant;
import com.quickbite.model.User;
import com.quickbite.repository.RestaurantRepository;
import com.quickbite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findByIsOpenTrue();
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    public List<Restaurant> searchByName(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name);
    }

    public Restaurant createRestaurant(Restaurant restaurant, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        restaurant.setOwner(owner);
        return restaurantRepository.save(restaurant);
    }

    public Restaurant updateRestaurant(Long id, Restaurant updated, String ownerEmail) {
        Restaurant existing = getRestaurantById(id);
        if (!existing.getOwner().getEmail().equals(ownerEmail)) {
            throw new RuntimeException("Not authorized to update this restaurant");
        }
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setAddress(updated.getAddress());
        existing.setCuisine(updated.getCuisine());
        existing.setImageUrl(updated.getImageUrl());
        existing.setOpen(updated.isOpen());
        return restaurantRepository.save(existing);
    }

    public void deleteRestaurant(Long id, String ownerEmail) {
        Restaurant existing = getRestaurantById(id);
        if (!existing.getOwner().getEmail().equals(ownerEmail)) {
            throw new RuntimeException("Not authorized to delete this restaurant");
        }
        restaurantRepository.deleteById(id);
    }
}
