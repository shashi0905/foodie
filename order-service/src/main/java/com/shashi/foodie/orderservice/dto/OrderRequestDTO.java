package com.shashi.foodie.orderservice.dto;

import com.shashi.foodie.orderservice.model.FoodItem;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class OrderRequestDTO {

    private Long customerId;
    private Double amount;
    private HashMap<FoodItem, Integer> orderItems;
}
