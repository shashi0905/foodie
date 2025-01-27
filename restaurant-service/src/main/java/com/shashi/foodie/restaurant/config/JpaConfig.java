package com.shashi.foodie.restaurant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.shashi.foodie.restaurant.repository")
public class JpaConfig {
}

