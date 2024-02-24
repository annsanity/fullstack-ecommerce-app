package com.shopengine.ecommerceuserservice.service;

import com.shopengine.ecommerceuserservice.dto.CartDTO;
import com.shopengine.ecommerceuserservice.dto.CartItemDTO;
import com.shopengine.ecommerceuserservice.model.Cart;
import com.shopengine.ecommerceuserservice.model.CartItem;
import com.shopengine.ecommerceuserservice.model.Product;
import com.shopengine.ecommerceuserservice.model.User;
import com.shopengine.ecommerceuserservice.repository.CartRepository;
import com.shopengine.ecommerceuserservice.repository.CartItemRepository;
import com.shopengine.ecommerceuserservice.repository.ProductRepository;
import com.shopengine.ecommerceuserservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public CartDTO getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        return mapToCartDTO(cart);
    }

    public CartDTO addItemToCart(Long userId, CartItemDTO cartItemDTO) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            cart.setUser(user);
        }

        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setCart(cart);

        cart.getItems().add(cartItem);
        cartRepository.save(cart);
        return mapToCartDTO(cart);
    }

    // Helper method to map Cart to CartDTO
    private CartDTO mapToCartDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setUserId(cart.getUser().getId());

        List<CartItemDTO> cartItemDTOs = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            CartItemDTO itemDTO = new CartItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setQuantity(item.getQuantity());
            cartItemDTOs.add(itemDTO);
        }

        cartDTO.setItems(cartItemDTOs);
        return cartDTO;
    }

    public CartDTO updateCartItem(Long userId, CartItemDTO cartItemDTO) {
        // Retrieve the cart by user ID
        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            throw new RuntimeException("Cart not found for user ID: " + userId);
        }

        // Find the cart item that matches the provided CartItemDTO ID
        boolean itemUpdated = false;
        for (CartItem cartItem : cart.getItems()) {
            if (cartItem.getId().equals(cartItemDTO.getId())) {
                // Update the quantity of the found item
                cartItem.setQuantity(cartItemDTO.getQuantity());
                itemUpdated = true;
                break;
            }
        }

        // If the item was not found, throw an exception or handle as needed
        if (!itemUpdated) {
            throw new RuntimeException("Item not found in cart for item ID: " + cartItemDTO.getId());
        }

        // Save the updated cart
        cartRepository.save(cart);

        // Return the updated cart as CartDTO
        return mapToCartDTO(cart);
    }

    public CartDTO removeItemFromCart(Long userId, Long productId) {
        // Retrieve the cart by user ID
        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            throw new RuntimeException("Cart not found for user ID: " + userId);
        }

        // Find and remove the cart item with the specified productId
        boolean itemRemoved = cart.getItems().removeIf(cartItem ->
                cartItem.getProduct().getId().equals(productId)
        );

        // If no item was removed, throw an exception or handle as needed
        if (!itemRemoved) {
            throw new RuntimeException("Item with product ID " + productId + " not found in cart");
        }

        // Save the updated cart
        cartRepository.save(cart);

        // Return the updated cart as CartDTO
        return mapToCartDTO(cart);
    }



}
