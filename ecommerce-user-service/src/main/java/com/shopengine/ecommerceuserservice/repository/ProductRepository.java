package com.shopengine.ecommerceuserservice.repository;

import com.shopengine.ecommerceuserservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByNameContaining(String name);

    List<Product> findByPriceBetween(double min, double max);

    List<Product> findByStockGreaterThan(int stock);

    List<Product> findByOrderByPriceAsc();

    List<Product> findByOrderByPriceDesc();
}

