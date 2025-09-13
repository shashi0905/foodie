package com.shashi.foodie.restaurant.service;

import com.shashi.foodie.restaurant.esrepo.RestaurantSearchRepository;
import com.shashi.foodie.restaurant.model.FoodItem;
import com.shashi.foodie.restaurant.model.FoodItemDocument;
import com.shashi.foodie.restaurant.model.Restaurant;
import com.shashi.foodie.restaurant.model.RestaurantDocument;
import com.shashi.foodie.restaurant.repository.FoodItemRepository;
import com.shashi.foodie.restaurant.repository.RestaurantRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    private static final Logger logger = LogManager.getLogger(RestaurantService.class);

    private final RestaurantRepository restaurantRepository;
    private final FoodItemRepository foodItemRepository;
    private final RestaurantSearchRepository restaurantSearchRepository;
    private final RestaurantMapper restaurantMapper;
    private final FoodItemMapper foodItemMapper;

    private final ElasticsearchTemplate elasticsearchTemplate;

    public RestaurantService(RestaurantRepository restaurantRepository, FoodItemRepository foodItemRepository,
                             RestaurantSearchRepository restaurantSearchRepository, RestaurantMapper restaurantMapper, FoodItemMapper foodItemMapper, ElasticsearchTemplate elasticsearchTemplate) {
        this.restaurantRepository = restaurantRepository;
        this.foodItemRepository = foodItemRepository;
        this.restaurantSearchRepository = restaurantSearchRepository;
        this.restaurantMapper = restaurantMapper;
        this.foodItemMapper = foodItemMapper;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

   public Restaurant get(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with ID: " + id));
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public List<FoodItem> getAllFoodItemsByRestaurant(Long restaurantId) {
        return foodItemRepository.findByRestaurantId(restaurantId);
    }

    @Transactional
    public Restaurant add(Restaurant restaurant) {
        // Save to MySQL
        Restaurant saved = restaurantRepository.save(restaurant);

        // Convert documents
        RestaurantDocument restaurantDocument = restaurantMapper.toDocument(restaurant);
        List<FoodItemDocument> foodItemDocuments = restaurant.getFoodItems().stream()
                .map(foodItemMapper::toDocument)
                .toList();

        // Bulk save to Elasticsearch
        List<Object> documents = new ArrayList<>();

        logger.log(Level.INFO,"Adding {} restaurant with {} fooditems to elastic",restaurantDocument.getName(), foodItemDocuments.size());
        documents.add(restaurantDocument);
        documents.addAll(foodItemDocuments);

        elasticsearchTemplate.save(documents);

        return saved;
    }

    public void delete(Restaurant restaurant) {
        restaurantRepository.delete(restaurant);
        restaurantSearchRepository.delete(restaurantMapper.toDocument(restaurant));
    }
}
