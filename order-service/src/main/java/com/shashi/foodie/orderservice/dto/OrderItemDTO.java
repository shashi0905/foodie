package com.shashi.foodie.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemDTO {

    private Long restaurantId;   // ID of the restaurant offering the food item
    private Long foodItemId;     // ID of the food item
    private String foodItemName; // Name of the food item
    private Double foodItemPrice; // Price per unit
    private Integer quantity;    // Quantity ordered
    private Double totalPrice;   // Optional, can be calculated in response

}

