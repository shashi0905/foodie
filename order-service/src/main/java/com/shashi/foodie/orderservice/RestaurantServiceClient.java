package com.shashi.foodie.orderservice;

import com.shashi.foodie.orderservice.dto.FoodItemDTO;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RestaurantServiceClient {

    private static Logger log;

    @Value("${restaurant.service.url}")
    private String restaurantServiceUrl;

    private final RestTemplate restTemplate;

    public RestaurantServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<FoodItemDTO> getFoodItems(Long restaurantId) {
        String url = restaurantServiceUrl + "/api/restaurants/" + restaurantId + "/food-items";

        log.info(url);
        ResponseEntity<List<FoodItemDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<FoodItemDTO>>() {}
        );

        return response.getBody();
    }
}

