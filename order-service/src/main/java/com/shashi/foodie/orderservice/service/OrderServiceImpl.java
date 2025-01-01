package com.shashi.foodie.orderservice.service;

import com.shashi.foodie.orderservice.RestaurantServiceClient;
import com.shashi.foodie.orderservice.dto.FoodItemDTO;
import com.shashi.foodie.orderservice.dto.OrderItemDTO;
import com.shashi.foodie.orderservice.dto.OrderRequestDTO;
import com.shashi.foodie.orderservice.dto.OrderResponseDTO;
import com.shashi.foodie.orderservice.model.Order;
import com.shashi.foodie.orderservice.model.OrderItem;
import com.shashi.foodie.orderservice.model.Status;
import com.shashi.foodie.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RestaurantServiceClient restaurantServiceClient;

    @Override
    public OrderResponseDTO getOrder(Long orderId) {
        // Fetch order entity from the database
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        // Map order entity to response DTO
        return mapToResponseDTO(order);
    }

    // Utility method to map Order entity to OrderResponseDTO
    private OrderResponseDTO mapToResponseDTO(Order order) {
        List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream().map(item -> {
            return new OrderItemDTO(
                    item.getRestaurantId(),
                    item.getFoodItemId(),
                    item.getFoodItemName(),
                    item.getFoodItemPrice(),
                    item.getQuantity(),
                    item.getTotalPrice()
            );
        }).collect(Collectors.toList());

        return new OrderResponseDTO(
                order.getId(),
                order.getCustomerId(),
                order.getOrderTime(),
                order.getTotalAmount(),
                orderItemDTOs
        );
    }

    @Override
    public List<OrderResponseDTO> getOrderByCustomer(Long customerId) {
        return List.of();
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest) {
        Order order = new Order();
        order.setCustomerId(orderRequest.getCustomerId());
        order.setOrderTime(LocalDateTime.now());

        double totalAmount = 0.0;

        for (OrderItemDTO itemDTO : orderRequest.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setRestaurantId(itemDTO.getRestaurantId());
            orderItem.setFoodItemId(itemDTO.getFoodItemId());
            orderItem.setFoodItemName(itemDTO.getFoodItemName());
            orderItem.setFoodItemPrice(itemDTO.getFoodItemPrice());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setTotalPrice(itemDTO.getFoodItemPrice() * itemDTO.getQuantity());
            totalAmount += orderItem.getTotalPrice();
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        order.setTotalAmount(totalAmount);

        // Save order to the database
        orderRepository.save(order);

        return mapToResponseDTO(order);
    }

   /* private OrderResponseDTO mapToResponseDTO(Order order) {
        return new OrderResponseDTO(order.getId(), order.getCustomerId(), order.getOrderTime(), order.getTotalAmount(), order.getOrderItems());
    }*/

}
