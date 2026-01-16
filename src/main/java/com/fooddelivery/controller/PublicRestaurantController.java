package com.fooddelivery.controller;

import com.fooddelivery.dto.RestaurantResponse;
import com.fooddelivery.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Public Restaurant", description = "Public endpoints for browsing restaurants (no authentication required)")
public class PublicRestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    @Operation(
            summary = "Get all restaurants with filters",
            description = "Retrieves a paginated list of restaurants with optional filtering by cuisine, city, and minimum rating. Supports sorting and pagination."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurants retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<Page<RestaurantResponse>> getAllRestaurants(
            @Parameter(description = "Filter by cuisine type (e.g., Italian, Chinese)")
            @RequestParam(required = false) String cuisine,
            @Parameter(description = "Filter by city name")
            @RequestParam(required = false) String city,
            @Parameter(description = "Minimum rating (e.g., 4.0)")
            @RequestParam(required = false) BigDecimal minRating,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field (id, name, rating)", example = "rating")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc, desc)", example = "desc")
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<RestaurantResponse> restaurants = restaurantService.getAllRestaurants(cuisine, city, minRating, pageable);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get restaurant by ID",
            description = "Retrieves detailed information about a specific restaurant including menu items and ratings."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant found"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public ResponseEntity<RestaurantResponse> getRestaurantById(
            @Parameter(description = "Restaurant ID", required = true, example = "1")
            @PathVariable Long id) {
        RestaurantResponse restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }

    @GetMapping("/cuisine/{cuisine}")
    public ResponseEntity<Page<RestaurantResponse>> getRestaurantsByCuisine(
            @PathVariable String cuisine,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantResponse> restaurants = restaurantService.getRestaurantsByCuisine(cuisine, pageable);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<Page<RestaurantResponse>> getRestaurantsByCity(
            @PathVariable String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantResponse> restaurants = restaurantService.getRestaurantsByCity(city, pageable);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<RestaurantResponse>> getNearbyRestaurants(
            @RequestParam Double lat,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5.0") Double radius) {
        List<RestaurantResponse> restaurants = restaurantService.getNearbyRestaurants(lat, longitude, radius);
        return ResponseEntity.ok(restaurants);
    }
}

