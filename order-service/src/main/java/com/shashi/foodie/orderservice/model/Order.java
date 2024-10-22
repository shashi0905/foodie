package com.shashi.foodie.orderservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
@Setter
@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    //private String description;

    //@Column(name = "items")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKeyColumn(name = "items")
    private HashMap<FoodItem, Integer> items;

    @Column(name = "cost")
    private double totalCost;

    @Column(name = "customerId")
    private Long customerId;

    @Enumerated(EnumType.STRING)  // Store enum as String in DB
    private Status status;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

}
