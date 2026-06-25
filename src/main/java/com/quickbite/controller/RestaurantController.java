package com.quickbite.controller;

import com.quickbite.model.Restaurant;
import com.quickbite.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAll() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> search(@RequestParam String name) {
        return ResponseEntity.ok(restaurantService.searchByName(name));
    }

    @PostMapping
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant,
                                              Authentication auth) {
        return ResponseEntity.ok(restaurantService.createRestaurant(restaurant, auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> update(@PathVariable Long id,
                                              @RequestBody Restaurant restaurant,
                                              Authentication auth) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, restaurant, auth.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        restaurantService.deleteRestaurant(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
