package com.shashi.foodie.orderservice.repository;

import com.shashi.foodie.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
