package com.shashi.foodie.orderservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * intermediary entity that represents the relationship between FoodItem and Order.
 * It stores the quantity of each FoodItem in the order
 */
@Entity
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Long restaurantId; // Reference to the restaurant

    private Long foodItemId; // Reference to the food item

    private String foodItemName;

    private Double foodItemPrice;

    private Integer quantity;

    private Double totalPrice;

    // Getters, Setters, and Utility Methods
}



