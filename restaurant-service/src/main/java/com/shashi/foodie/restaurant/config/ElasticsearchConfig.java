package com.shashi.foodie.restaurant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.shashi.foodie.restaurant.esrepo")
public class ElasticsearchConfig {
}
