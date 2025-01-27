package com.shashi.foodie.restaurant.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "restaurant")
@Getter
@Setter
public class RestaurantDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Keyword)
    private String address;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<FoodItemDocument> foodItems;
}
