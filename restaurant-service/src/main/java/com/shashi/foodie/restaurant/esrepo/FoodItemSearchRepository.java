package com.shashi.foodie.restaurant.esrepo;

import com.shashi.foodie.restaurant.model.FoodItemDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface FoodItemSearchRepository extends ElasticsearchRepository<FoodItemDocument, Long> {
    List<FoodItemDocument> findByNameContaining(String name);
}

