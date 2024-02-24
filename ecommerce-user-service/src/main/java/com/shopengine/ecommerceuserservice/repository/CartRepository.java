package com.shopengine.ecommerceuserservice.repository;

import com.shopengine.ecommerceuserservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long userId);
}
