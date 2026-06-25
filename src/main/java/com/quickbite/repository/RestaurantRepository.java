package com.quickbite.repository;

import com.quickbite.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByOwnerId(Long ownerId);
    List<Restaurant> findByIsOpenTrue();
    List<Restaurant> findByCuisineContainingIgnoreCase(String cuisine);
    List<Restaurant> findByNameContainingIgnoreCase(String name);
}
