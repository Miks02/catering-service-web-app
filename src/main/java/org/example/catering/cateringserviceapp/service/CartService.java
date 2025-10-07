package org.example.catering.cateringserviceapp.service;

import org.example.catering.cateringserviceapp.models.Cart;
import org.example.catering.cateringserviceapp.models.CartItem;
import org.example.catering.cateringserviceapp.repository.CartItemRepository;
import org.example.catering.cateringserviceapp.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Cart getCart(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });
    }

    public void addToCart(String userId, CartItem item) {
        Cart cart = getCart(userId);

        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartIdAndProductId(cart.getId(), item.getProduct().getId());

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            item.setCart(cart);
            cartItemRepository.save(item);
        }
    }

    public void removeFromCart(String userId, Long itemId) {
        Cart cart = getCart(userId);
        cartItemRepository.findById(itemId).ifPresent(item -> {
            if (item.getCart().getId().equals(cart.getId())) {
                cartItemRepository.delete(item);
            }
        });
    }

    public void clearCart(String userId) {
        Cart cart = getCart(userId);
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        cartItemRepository.deleteAll(items);
    }

}
