package com.shashi.foodie.orderservice.service;

import com.shashi.foodie.orderservice.dto.OrderRequestDTO;
import com.shashi.foodie.orderservice.dto.OrderResponseDTO;
import com.shashi.foodie.orderservice.model.Order;
import com.shashi.foodie.orderservice.model.Status;
import com.shashi.foodie.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService{

    @Autowired
    OrderRepository orderRepository;

    public OrderResponseDTO getOrder(Long id) {
        //Optional<Order> order = orderRepository.findById(id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found!"));
        //return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

        return new OrderResponseDTO(order.getId(), order.getStatus().name(), order.getCreatedAt(), order.getTotalCost(), order.getItems());
    }

    @Override
    public List<OrderResponseDTO> getOrderByCustomer(Long customerId) {
        return List.of();
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        //order.setProductName(orderRequestDTO.getProductName());
        order.setTotalCost(orderRequestDTO.getAmount());
        order.setItems(orderRequestDTO.getOrderItems());
        order.setCustomerId(orderRequestDTO.getCustomerId());
        order.setStatus(Status.NEW);

        // Save entity to the database
        Order savedOrder = orderRepository.save(order);

        // Convert Entity to DTO
        return new OrderResponseDTO(savedOrder.getId(), savedOrder.getStatus().name(), LocalDateTime.now(), savedOrder.getTotalCost(), savedOrder.getItems());
    }
}
