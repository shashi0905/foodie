package com.shashi.foodie.restaurant.service;

import com.shashi.foodie.restaurant.model.Cart;
import com.shashi.foodie.restaurant.model.CartItem;
import com.shashi.foodie.restaurant.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart addItems(Long customerId, CartItem cartItem) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElse(new Cart());
        cart.setCustomerId(customerId);
        cart.getCartItems().add(cartItem);
        cartItem.setCart(cart);
        return cartRepository.save(cart);
    }

    public Cart removeItems(Long customerId, Long cartItemId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getCartItems().removeIf(item -> item.getId().equals(cartItemId));
        return cartRepository.save(cart);
    }

    public Optional<Cart> getCartByCustomerId(Long customerId) {
        return cartRepository.findByCustomerId(customerId);
    }

}
