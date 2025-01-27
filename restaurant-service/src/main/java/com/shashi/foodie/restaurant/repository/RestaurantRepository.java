package com.shashi.foodie.restaurant.repository;

import com.shashi.foodie.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
