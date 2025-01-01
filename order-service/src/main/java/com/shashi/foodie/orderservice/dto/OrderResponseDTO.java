package com.shashi.foodie.orderservice.dto;

import com.shashi.foodie.orderservice.model.FoodItem;
import com.shashi.foodie.orderservice.model.Order;
import com.shashi.foodie.orderservice.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class OrderResponseDTO {
    private Long orderId; // Generated ID of the order
    private Long customerId; // Customer who placed the order
    private LocalDateTime orderTime; // Timestamp when the order was placed
    private Double totalAmount; // Total cost of the order
    private List<OrderItemDTO> orderItems; // List of items in the order

   /* public OrderResponseDTO(Order order) {
        this.orderId = order.getId();
        this.customerId =order.getCustomerId();
        this.orderTime = order.getOrderTime();
        this.totalAmount =order.getTotalAmount();
        this.orderItems =order.getOrderItems();
    }*/

   /* @Getter
    public static class OrderItemResponseDTO {
        private Long foodItemId;
        private int quantity;
        private double price;

        // Constructor, Getters, Setters
    }*/
}
