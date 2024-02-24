package com.shopengine.ecommerceuserservice.controller;

import com.shopengine.ecommerceuserservice.dto.CartDTO;
import com.shopengine.ecommerceuserservice.dto.CartItemDTO;
import com.shopengine.ecommerceuserservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Endpoint to retrieve the cart by user ID
    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable Long userId) {
        CartDTO cartDTO = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartDTO);
    }

    // Endpoint to add an item to the cart
    @PostMapping("/{userId}/add")
    public ResponseEntity<CartDTO> addItemToCart(@PathVariable Long userId, @RequestBody CartItemDTO cartItemDTO) {
        CartDTO cartDTO = cartService.addItemToCart(userId, cartItemDTO);
        return ResponseEntity.ok(cartDTO);
    }

    // Optional: Endpoint to update item quantity
    @PutMapping("/{userId}/update")
    public ResponseEntity<CartDTO> updateCartItem(@PathVariable Long userId, @RequestBody CartItemDTO cartItemDTO) {
        CartDTO cartDTO = cartService.updateCartItem(userId, cartItemDTO);
        return ResponseEntity.ok(cartDTO);
    }

    // Optional: Endpoint to remove an item from the cart
    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<CartDTO> removeItemFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        CartDTO cartDTO = cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok(cartDTO);
    }
}
