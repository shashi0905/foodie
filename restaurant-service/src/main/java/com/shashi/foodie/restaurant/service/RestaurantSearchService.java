package com.shashi.foodie.restaurant.service;

import com.shashi.foodie.restaurant.esrepo.RestaurantSearchRepository;
import com.shashi.foodie.restaurant.model.Restaurant;
import com.shashi.foodie.restaurant.model.RestaurantDocument;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantSearchService {

    private final RestaurantSearchRepository restaurantSearchRepository;

    public RestaurantSearchService(RestaurantSearchRepository restaurantSearchRepository) {
        this.restaurantSearchRepository = restaurantSearchRepository;
    }

    public List<RestaurantDocument> searchRestaurant(String query) {
        return restaurantSearchRepository.findRestaurantByNameContaining(query);
    }
}
