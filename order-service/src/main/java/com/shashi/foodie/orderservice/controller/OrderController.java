package com.shashi.foodie.orderservice.controller;

import com.shashi.foodie.orderservice.dto.OrderRequestDTO;
import com.shashi.foodie.orderservice.dto.OrderResponseDTO;
import com.shashi.foodie.orderservice.model.Order;
import com.shashi.foodie.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping
    public List<Order> getOrders() {
        return List.of();   //TO-DO
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long id) {
        try {
            OrderResponseDTO orderResponseDTO = orderService.getOrder(id);
            return ResponseEntity.ok(orderResponseDTO);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }}

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO _order = orderService.createOrder(orderRequestDTO);
        return new ResponseEntity<>(_order, HttpStatus.CREATED);
    }
}
