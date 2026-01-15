package com.fooddelivery.controller;

import com.fooddelivery.dto.CartItemRequest;
import com.fooddelivery.dto.CartResponse;
import com.fooddelivery.service.CartService;
import com.fooddelivery.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private SecurityUtil securityUtil;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(Authentication authentication) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        CartResponse response = cartService.getCart(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addItemToCart(
            Authentication authentication,
            @Valid @RequestBody CartItemRequest request) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        CartResponse response = cartService.addItemToCart(userId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<CartResponse> updateCartItemQuantity(
            Authentication authentication,
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        CartResponse response = cartService.updateCartItemQuantity(userId, id, quantity);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeCartItem(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        cartService.removeCartItem(userId, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}

