package com.shashi.foodie.orderservice.service;

import com.shashi.foodie.orderservice.dto.OrderRequestDTO;
import com.shashi.foodie.orderservice.dto.OrderResponseDTO;
import com.shashi.foodie.orderservice.model.Order;

import java.util.List;

public interface OrderService {

    public OrderResponseDTO getOrder(Long id);

    public List<OrderResponseDTO> getOrderByCustomer(Long customerId);

    //public List<Order> getAllOrders();

    public OrderResponseDTO createOrder(OrderRequestDTO order);
}
