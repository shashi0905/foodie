package com.shashi.foodie.restaurant.controller;

import com.shashi.foodie.restaurant.model.FoodItem;
import com.shashi.foodie.restaurant.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public ResponseEntity<List<FoodItem>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(searchService.searchFoodItems(keyword));
    }
}

