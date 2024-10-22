package com.shashi.foodie.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FoodItem {
    private int itemId;
    private String name;
    private Category category;
    //private addon
    private double price;
    private String description;
    //image
    private int rating;
    private int restId;

}
