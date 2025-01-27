package com.shashi.foodie.restaurant.controller;

import com.shashi.foodie.restaurant.model.Cart;
import com.shashi.foodie.restaurant.model.CartItem;
import com.shashi.foodie.restaurant.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{customerId}")
    public Cart addItemToCart(@PathVariable Long customerId, @RequestBody CartItem cartItem) {
        return cartService.addItems(customerId, cartItem);
    }

    @DeleteMapping("/{customerId}/{cartItemId}")
    public Cart removeItemFromCart(@PathVariable Long customerId, @PathVariable Long cartItemId) {
        return cartService.removeItems(customerId, cartItemId);
    }

    @GetMapping("/{customerId}")
    public Cart getCart(@PathVariable Long customerId) {
        return cartService.getCartByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }
}

