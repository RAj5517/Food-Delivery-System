package com.fooddelivery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "delivery_partner")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = false;

    @Column(name = "current_lat")
    private Double currentLat;

    @Column(name = "current_long")
    private Double currentLong;

    // Relationships
    @OneToMany(mappedBy = "deliveryPartner", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    public enum VehicleType {
        BIKE,
        SCOOTER,
        CAR,
        BICYCLE
    }
}

