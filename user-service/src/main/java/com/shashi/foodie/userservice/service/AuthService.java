package com.shashi.foodie.userservice.service;

import com.shashi.foodie.userservice.dto.AuthResponse;
import com.shashi.foodie.userservice.dto.LoginRequest;
import com.shashi.foodie.userservice.dto.RegisterRequest;
import com.shashi.foodie.userservice.dto.UserDTO;

public interface AuthService {

    AuthResponse register(RegisterRequest registerRequest);

    AuthResponse login(LoginRequest loginRequest);

    UserDTO getUserProfile(Long userId);
}
