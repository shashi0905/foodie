package com.shashi.foodie.restaurant.service;

import com.shashi.foodie.restaurant.model.FoodItem;
import com.shashi.foodie.restaurant.model.FoodItemDocument;
import com.shashi.foodie.restaurant.model.Restaurant;
import com.shashi.foodie.restaurant.model.RestaurantDocument;
import org.springframework.stereotype.Service;

@Service
public class FoodItemMapper {

    public FoodItemDocument toDocument(FoodItem foodItem) {
        FoodItemDocument document = new FoodItemDocument();
        document.setId(foodItem.getId());
        document.setName(foodItem.getName());
        document.setPrice(foodItem.getPrice());

        Restaurant restaurant = foodItem.getRestaurant();
        if (restaurant != null) {
            RestaurantDocument restaurantDocument = new RestaurantDocument();
            restaurantDocument.setId(restaurant.getId());
            restaurantDocument.setName(restaurant.getName());
            document.setRestaurant(restaurantDocument);
        }

        return document;
    }

    public FoodItem toEntity(FoodItemDocument document) {
        FoodItem foodItem = new FoodItem();
        foodItem.setId(document.getId());
        foodItem.setName(document.getName());
        foodItem.setPrice(document.getPrice());
        // Restaurant transformation logic here if needed
        return foodItem;
    }
}

