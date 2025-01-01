package com.shashi.foodie.orderservice.dto;

import com.shashi.foodie.orderservice.model.FoodItem;
import com.shashi.foodie.orderservice.model.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class OrderRequestDTO {

    private Long customerId;
    private List<OrderItemDTO> orderItems;

}
