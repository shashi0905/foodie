package com.shashi.foodie.restaurant.service;

import com.shashi.foodie.restaurant.model.FoodItemDocument;
import com.shashi.foodie.restaurant.esrepo.FoodItemSearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemSearchService {

    private final FoodItemSearchRepository foodItemSearchRepository;

    public FoodItemSearchService(FoodItemSearchRepository foodItemSearchRepository) {
        this.foodItemSearchRepository = foodItemSearchRepository;
    }

    public List<FoodItemDocument> searchFoodItems(String query) {
        //return (List<FoodItemDocument>) foodItemSearchRepository.findAll();
        return foodItemSearchRepository.findByNameContaining(query);
    }
}

