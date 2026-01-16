package com.fooddelivery.service;

import com.fooddelivery.dto.AddressResponse;
import com.fooddelivery.dto.AdminUserResponse;
import com.fooddelivery.dto.OrderItemResponse;
import com.fooddelivery.dto.OrderResponse;
import com.fooddelivery.dto.RestaurantResponse;
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderItem;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.model.User;
import com.fooddelivery.repository.OrderRepository;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private OrderRepository orderRepository;

    // User Management

    public Page<AdminUserResponse> getAllUsers(User.Role role, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users;
        
        if (role != null) {
            users = userRepository.findByRole(role, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }
        
        return users.map(this::convertToAdminUserResponse);
    }

    public AdminUserResponse activateUser(Long userId, Boolean isActive) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        user.setIsActive(isActive);
        user = userRepository.save(user);
        
        return convertToAdminUserResponse(user);
    }

    // Restaurant Management

    public Page<RestaurantResponse> getPendingRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurants = restaurantRepository.findByIsApproved(false, pageable);
        return restaurants.map(this::convertToRestaurantResponse);
    }

    @Transactional
    public RestaurantResponse approveRestaurant(Long restaurantId, Boolean isApproved) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));
        
        restaurant.setIsApproved(isApproved);
        restaurant = restaurantRepository.save(restaurant);
        
        return convertToRestaurantResponse(restaurant);
    }

    // Order Management

    public Page<OrderResponse> getAllOrders(Order.OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders;
        
        if (status != null) {
            orders = orderRepository.findByStatus(status, pageable);
        } else {
            orders = orderRepository.findAll(pageable);
        }
        
        return orders.map(this::convertToOrderResponse);
    }

    private AdminUserResponse convertToAdminUserResponse(User user) {
        AdminUserResponse response = new AdminUserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());
        response.setIsActive(user.getIsActive());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    private RestaurantResponse convertToRestaurantResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        response.setId(restaurant.getId());
        response.setName(restaurant.getName());
        response.setCuisine(restaurant.getCuisine());
        response.setAddress(restaurant.getAddress());
        response.setLat(restaurant.getLat());
        response.setLongitude(restaurant.getLongitude());
        response.setIsOpen(restaurant.getIsOpen());
        response.setAvgPrepTime(restaurant.getAvgPrepTime());
        response.setRating(restaurant.getRating());
        response.setIsApproved(restaurant.getIsApproved());
        if (restaurant.getUser() != null) {
            response.setEmail(restaurant.getUser().getEmail());
            response.setPhone(restaurant.getUser().getPhone());
        }
        return response;
    }

    private OrderResponse convertToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerId(order.getCustomer().getId());
        response.setCustomerName(order.getCustomer().getName());
        response.setRestaurantId(order.getRestaurant().getId());
        response.setRestaurantName(order.getRestaurant().getName());
        
        if (order.getDeliveryPartner() != null) {
            response.setDeliveryPartnerId(order.getDeliveryPartner().getId());
            response.setDeliveryPartnerName(order.getDeliveryPartner().getName());
        }
        
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setPaymentStatus(order.getPaymentStatus());
        response.setOrderDate(order.getOrderDate());
        response.setDeliveredDate(order.getDeliveredDate());

        // Convert address
        if (order.getAddress() != null) {
            AddressResponse addressResponse = new AddressResponse();
            addressResponse.setId(order.getAddress().getId());
            addressResponse.setStreet(order.getAddress().getStreet());
            addressResponse.setCity(order.getAddress().getCity());
            addressResponse.setPincode(order.getAddress().getPincode());
            addressResponse.setLat(order.getAddress().getLat());
            addressResponse.setLongitude(order.getAddress().getLongitude());
            addressResponse.setAddressType(order.getAddress().getAddressType());
            addressResponse.setIsDefault(order.getAddress().getIsDefault());
            response.setAddress(addressResponse);
        }

        // Convert order items
        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(this::convertToOrderItemResponse)
                .collect(Collectors.toList());
        response.setItems(items);

        return response;
    }

    private OrderItemResponse convertToOrderItemResponse(OrderItem orderItem) {
        OrderItemResponse response = new OrderItemResponse();
        response.setId(orderItem.getId());
        response.setMenuItemId(orderItem.getMenuItem().getId());
        response.setMenuItemName(orderItem.getMenuItem().getName());
        response.setQuantity(orderItem.getQuantity());
        response.setPrice(orderItem.getPrice());
        response.setSubtotal(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        return response;
    }
}

