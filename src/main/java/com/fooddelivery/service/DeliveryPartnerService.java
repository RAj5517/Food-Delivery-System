package com.fooddelivery.service;

import com.fooddelivery.dto.DeliveryPartnerResponse;
import com.fooddelivery.dto.EarningsResponse;
import com.fooddelivery.dto.UpdateLocationRequest;
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.model.DeliveryPartner;
import com.fooddelivery.model.Order;
import com.fooddelivery.repository.DeliveryPartnerRepository;
import com.fooddelivery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeliveryPartnerService {

    @Autowired
    private DeliveryPartnerRepository deliveryPartnerRepository;

    @Autowired
    private OrderRepository orderRepository;

    // Delivery partner earns 10% of order total as commission
    private static final BigDecimal DELIVERY_COMMISSION_RATE = new BigDecimal("0.10");

    public DeliveryPartnerResponse getProfile(Long userId) {
        DeliveryPartner deliveryPartner = deliveryPartnerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery partner not found"));

        return convertToResponse(deliveryPartner);
    }

    @Transactional
    public DeliveryPartnerResponse toggleAvailability(Long userId) {
        DeliveryPartner deliveryPartner = deliveryPartnerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery partner not found"));

        deliveryPartner.setIsAvailable(!deliveryPartner.getIsAvailable());
        deliveryPartner = deliveryPartnerRepository.save(deliveryPartner);

        return convertToResponse(deliveryPartner);
    }

    @Transactional
    public DeliveryPartnerResponse updateLocation(Long userId, UpdateLocationRequest request) {
        DeliveryPartner deliveryPartner = deliveryPartnerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery partner not found"));

        deliveryPartner.setCurrentLat(request.getLat());
        deliveryPartner.setCurrentLong(request.getLongitude());
        deliveryPartner = deliveryPartnerRepository.save(deliveryPartner);

        return convertToResponse(deliveryPartner);
    }

    public EarningsResponse getEarnings(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        DeliveryPartner deliveryPartner = deliveryPartnerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery partner not found"));

        List<Order> deliveredOrders;

        if (startDate != null && endDate != null) {
            deliveredOrders = orderRepository.findByDeliveryPartnerIdAndStatusAndOrderDateBetween(
                    deliveryPartner.getId(),
                    Order.OrderStatus.DELIVERED,
                    startDate,
                    endDate
            );
        } else if (startDate != null) {
            deliveredOrders = orderRepository.findByDeliveryPartnerIdAndStatusAndOrderDateAfter(
                    deliveryPartner.getId(),
                    Order.OrderStatus.DELIVERED,
                    startDate
            );
        } else {
            deliveredOrders = orderRepository.findByDeliveryPartnerIdAndStatus(
                    deliveryPartner.getId(),
                    Order.OrderStatus.DELIVERED
            );
        }

        BigDecimal totalEarnings = deliveredOrders.stream()
                .map(order -> order.getTotalAmount().multiply(DELIVERY_COMMISSION_RATE))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        Long totalDeliveries = (long) deliveredOrders.size();

        BigDecimal averageEarningPerDelivery = totalDeliveries > 0
                ? totalEarnings.divide(BigDecimal.valueOf(totalDeliveries), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        EarningsResponse response = new EarningsResponse();
        response.setTotalEarnings(totalEarnings);
        response.setTotalDeliveries(totalDeliveries);
        response.setAverageEarningPerDelivery(averageEarningPerDelivery);
        response.setStartDate(startDate);
        response.setEndDate(endDate);

        return response;
    }

    private DeliveryPartnerResponse convertToResponse(DeliveryPartner deliveryPartner) {
        DeliveryPartnerResponse response = new DeliveryPartnerResponse();
        response.setId(deliveryPartner.getId());
        response.setUserId(deliveryPartner.getUser().getId());
        response.setName(deliveryPartner.getName());
        response.setVehicleType(deliveryPartner.getVehicleType());
        response.setIsAvailable(deliveryPartner.getIsAvailable());
        response.setCurrentLat(deliveryPartner.getCurrentLat());
        response.setCurrentLong(deliveryPartner.getCurrentLong());
        response.setEmail(deliveryPartner.getUser().getEmail());
        response.setPhone(deliveryPartner.getUser().getPhone());
        return response;
    }
}

