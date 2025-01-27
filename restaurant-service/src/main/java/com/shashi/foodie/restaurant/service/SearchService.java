package com.shashi.foodie.restaurant.service;

import com.shashi.foodie.restaurant.esrepo.FoodItemSearchRepository;
import com.shashi.foodie.restaurant.model.FoodItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class SearchService {

    private static final Logger logger = Logger.getLogger("SearchService");
    private final FoodItemSearchRepository searchRepository;
    private final FoodItemMapper foodItemMapper;

    public SearchService(FoodItemSearchRepository searchRepository, FoodItemMapper foodItemMapper) {
        this.searchRepository = searchRepository;
        this.foodItemMapper = foodItemMapper;
    }

    public List<FoodItem> searchFoodItems(String keyword) {
        logger.info("Searching Food Items matching with keyword "+ keyword);
        return searchRepository.findByNameContaining(keyword).stream().map(foodItemMapper::toEntity).toList();
    }
}

