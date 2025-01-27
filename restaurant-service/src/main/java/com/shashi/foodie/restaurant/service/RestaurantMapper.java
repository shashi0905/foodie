package com.shashi.foodie.restaurant.service;

import com.shashi.foodie.restaurant.model.FoodItem;
import com.shashi.foodie.restaurant.model.FoodItemDocument;
import com.shashi.foodie.restaurant.model.Restaurant;
import com.shashi.foodie.restaurant.model.RestaurantDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantMapper {

    private final FoodItemMapper foodItemMapper;

    public RestaurantMapper(FoodItemMapper foodItemMapper) {
        this.foodItemMapper = foodItemMapper;
    }

    public RestaurantDocument toDocument(Restaurant restaurant) {
        RestaurantDocument document = new RestaurantDocument();
        document.setId(restaurant.getId());
        document.setName(restaurant.getName());
        document.setAddress(restaurant.getAddress());
        List<FoodItemDocument> foodItemDocumentList = new ArrayList<>();
        for(FoodItem item : restaurant.getFoodItems()) {
            foodItemDocumentList.add(foodItemMapper.toDocument(item));
        }
        document.setFoodItems(foodItemDocumentList);
        return document;
    }

    public Restaurant toEntity(RestaurantDocument document) {
        Restaurant entity = new Restaurant();
        entity.setId(document.getId());
        entity.setAddress(document.getAddress());
        entity.setName(document.getName());
        List<FoodItem> foodItemList = new ArrayList<>();
        for(FoodItemDocument foodItemDocument : document.getFoodItems()) {
            foodItemList.add(foodItemMapper.toEntity(foodItemDocument));
        }
        entity.setFoodItems(foodItemList);
        return entity;
    }
}
