package com.fooddelivery.service;

import com.fooddelivery.dto.CartItemRequest;
import com.fooddelivery.dto.CartItemResponse;
import com.fooddelivery.dto.CartResponse;
import com.fooddelivery.exception.BadRequestException;
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.model.*;
import com.fooddelivery.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Transactional
    public CartResponse addItemToCart(Long userId, CartItemRequest request) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        if (!menuItem.getIsAvailable()) {
            throw new BadRequestException("Menu item is not available");
        }

        Restaurant restaurant = menuItem.getRestaurant();

        // Get or create cart
        Cart cart = cartRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(customer);
                    newCart.setRestaurant(restaurant);
                    return cartRepository.save(newCart);
                });

        // Check if cart already has items from a different restaurant
        if (cart.getRestaurant() != null && !cart.getRestaurant().getId().equals(restaurant.getId())) {
            throw new BadRequestException("Cannot add items from different restaurants. Please clear your cart first.");
        }

        // Update restaurant if cart was empty
        if (cart.getRestaurant() == null) {
            cart.setRestaurant(restaurant);
            cart = cartRepository.save(cart);
        }

        // Check if item already exists in cart
        CartItem existingItem = cartItemRepository.findByCartIdAndMenuItemId(cart.getId(), menuItem.getId())
                .orElse(null);

        if (existingItem != null) {
            // Update quantity
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            existingItem.setPrice(menuItem.getPrice());
            cartItemRepository.save(existingItem);
        } else {
            // Add new item
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setMenuItem(menuItem);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(menuItem.getPrice());
            cartItemRepository.save(cartItem);
        }

        return getCart(userId);
    }

    public CartResponse getCart(Long userId) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Cart cart = cartRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(customer);
                    return cartRepository.save(newCart);
                });

        return convertToCartResponse(cart);
    }

    @Transactional
    public CartResponse updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Cart cart = cartRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem cartItem = cartItemRepository.findByIdAndCartId(cartItemId, cart.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        return getCart(userId);
    }

    @Transactional
    public void removeCartItem(Long userId, Long cartItemId) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Cart cart = cartRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem cartItem = cartItemRepository.findByIdAndCartId(cartItemId, cart.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void clearCart(Long userId) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Cart cart = cartRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cartItemRepository.deleteByCartId(cart.getId());
    }

    private CartResponse convertToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setCreatedAt(cart.getCreatedAt());

        if (cart.getRestaurant() != null) {
            response.setRestaurantId(cart.getRestaurant().getId());
            response.setRestaurantName(cart.getRestaurant().getName());
        }

        List<CartItemResponse> items = cart.getCartItems().stream()
                .map(this::convertToCartItemResponse)
                .collect(Collectors.toList());
        response.setItems(items);

        BigDecimal total = items.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        response.setTotalAmount(total);

        return response;
    }

    private CartItemResponse convertToCartItemResponse(CartItem cartItem) {
        CartItemResponse response = new CartItemResponse();
        response.setId(cartItem.getId());
        response.setMenuItemId(cartItem.getMenuItem().getId());
        response.setMenuItemName(cartItem.getMenuItem().getName());
        response.setQuantity(cartItem.getQuantity());
        response.setPrice(cartItem.getPrice());
        response.setSubtotal(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        return response;
    }
}

