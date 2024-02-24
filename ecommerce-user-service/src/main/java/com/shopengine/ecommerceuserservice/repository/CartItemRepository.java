package com.shopengine.ecommerceuserservice.repository;

import com.shopengine.ecommerceuserservice.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}