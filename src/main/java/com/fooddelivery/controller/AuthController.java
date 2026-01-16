package com.fooddelivery.controller;

import com.fooddelivery.dto.*;
import com.fooddelivery.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register/customer")
    @Operation(
            summary = "Register a new customer",
            description = "Creates a new customer account with email, password, name, and phone number. Returns JWT token upon successful registration."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Customer registered successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "token": "eyJhbGciOiJIUzUxMiJ9...",
                                        "userId": 1,
                                        "email": "customer@example.com",
                                        "role": "CUSTOMER",
                                        "expiresIn": 86400000
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Email already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<AuthResponse> registerCustomer(@Valid @RequestBody RegisterCustomerRequest request) {
        AuthResponse response = authService.registerCustomer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/restaurant")
    @Operation(
            summary = "Register a new restaurant",
            description = "Creates a new restaurant account. Restaurant must be approved by admin before it can be active. Returns JWT token upon successful registration."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurant registered successfully"),
            @ApiResponse(responseCode = "409", description = "Email already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<AuthResponse> registerRestaurant(@Valid @RequestBody RegisterRestaurantRequest request) {
        AuthResponse response = authService.registerRestaurant(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/delivery")
    @Operation(
            summary = "Register a new delivery partner",
            description = "Creates a new delivery partner account with vehicle type information. Returns JWT token upon successful registration."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Delivery partner registered successfully"),
            @ApiResponse(responseCode = "409", description = "Email already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<AuthResponse> registerDelivery(@Valid @RequestBody RegisterDeliveryRequest request) {
        AuthResponse response = authService.registerDelivery(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(
            summary = "User login",
            description = "Authenticates a user with email and password. Returns JWT token that should be used in Authorization header for protected endpoints. " +
                    "Format: 'Bearer <token>'"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "token": "eyJhbGciOiJIUzUxMiJ9...",
                                        "userId": 1,
                                        "email": "user@example.com",
                                        "role": "CUSTOMER",
                                        "expiresIn": 86400000
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Invalid email or password"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}

