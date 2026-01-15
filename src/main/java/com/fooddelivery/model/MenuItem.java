package com.fooddelivery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu_item", indexes = {
    @Index(name = "idx_menu_item_restaurant", columnList = "restaurant_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "image")
    private String image;

    @Column(name = "is_veg", nullable = false)
    private Boolean isVeg = true;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Relationships
    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
}

