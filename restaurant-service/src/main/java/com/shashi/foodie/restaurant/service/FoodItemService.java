package com.shashi.foodie.restaurant.service;

import com.shashi.foodie.restaurant.model.FoodItem;
import com.shashi.foodie.restaurant.repository.FoodItemRepository;
import com.shashi.foodie.restaurant.esrepo.FoodItemSearchRepository;
import org.springframework.stereotype.Service;

@Service
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;
    private final FoodItemSearchRepository searchRepository;
    private final FoodItemMapper foodItemMapper;

    public FoodItemService(FoodItemRepository foodItemRepository, FoodItemSearchRepository searchRepository,
                           FoodItemMapper foodItemMapper) {
        this.foodItemRepository = foodItemRepository;
        this.searchRepository = searchRepository;
        this.foodItemMapper =foodItemMapper;
    }

    public FoodItem addFoodItem(FoodItem foodItem) {
        FoodItem savedItem = foodItemRepository.save(foodItem);
        searchRepository.save(foodItemMapper.toDocument(savedItem)); // Sync to Elasticsearch
        return savedItem;
    }

    public void deleteFoodItem(Long id) {
        foodItemRepository.deleteById(id);
        searchRepository.deleteById(id); // Remove from Elasticsearch
    }
}

