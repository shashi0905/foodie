package com.shashi.foodie.orderservice.dto;

import com.shashi.foodie.orderservice.model.FoodItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;

@AllArgsConstructor
@Getter
@Setter
public class OrderResponseDTO {

    private Long id;
    private String status;
    private LocalDateTime createdAt;
    private Double amount;
    private HashMap<FoodItem, Integer> orderItems;
}
