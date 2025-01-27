package com.shashi.foodie.restaurant.controller;

import com.shashi.foodie.restaurant.model.FoodItem;
import com.shashi.foodie.restaurant.model.Restaurant;
import com.shashi.foodie.restaurant.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/{restaurantId}/food-items")
    public List<FoodItem> getFoodItemsByRestaurant(@PathVariable Long restaurantId) {
        return restaurantService.getAllFoodItemsByRestaurant(restaurantId);
    }

    @PostMapping
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant savedRes = restaurantService.add(restaurant);
        return new ResponseEntity<>(savedRes, HttpStatus.CREATED);
    }

   @DeleteMapping("/{restaurantId}")
    public void delete(@PathVariable Long restaurantId) {
        Restaurant restaurant = restaurantService.get(restaurantId);
        restaurantService.delete(restaurant);
    }

}

