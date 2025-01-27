package com.shashi.foodie.restaurant.esrepo;

import com.shashi.foodie.restaurant.model.Restaurant;
import com.shashi.foodie.restaurant.model.RestaurantDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface RestaurantSearchRepository extends ElasticsearchRepository<RestaurantDocument, Long> {
    List<RestaurantDocument> findRestaurantByNameContaining(String name);
}
