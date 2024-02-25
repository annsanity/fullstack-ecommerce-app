package com.shopengine.ecommerceuserservice.repository;

import com.shopengine.ecommerceuserservice.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
