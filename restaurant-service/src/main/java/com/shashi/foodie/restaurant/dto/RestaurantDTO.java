package com.shashi.foodie.restaurant.dto;

import com.shashi.foodie.restaurant.model.FoodItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RestaurantDTO {

    private Long id;

    private String name;
    private String address;
    private List<FoodItemDTO> foodItems;
}
