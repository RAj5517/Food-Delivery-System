package com.fooddelivery.repository;

import com.fooddelivery.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId);
    Optional<CartItem> findByIdAndCartId(Long id, Long cartId);
    Optional<CartItem> findByCartIdAndMenuItemId(Long cartId, Long menuItemId);
    void deleteByCartId(Long cartId);
}

