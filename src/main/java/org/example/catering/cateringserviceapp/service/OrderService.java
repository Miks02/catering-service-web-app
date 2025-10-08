package org.example.catering.cateringserviceapp.service;

import jakarta.transaction.Transactional;
import org.example.catering.cateringserviceapp.enums.DeliveryStatus;
import org.example.catering.cateringserviceapp.models.*;
import org.example.catering.cateringserviceapp.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final AppUserService appUserService;
    private final org.example.catering.cateringserviceapp.repository.AppUserRepository appUserRepository;

    public record CreateOrderResult(boolean discountApplied, double finalPrice) {
    }

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        CartService cartService,
                        AppUserService appUserService,
                        org.example.catering.cateringserviceapp.repository.AppUserRepository appUserRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.appUserService = appUserService;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public CreateOrderResult createOrder(Long userId, String shippingAddress) {
        var userOpt = appUserService.getUserById(userId);
        if (userOpt.isEmpty()) throw new RuntimeException("Korisnik nije pronađen");
        AppUser user = userOpt.get();

        Cart cart = cartService.getCart(userId);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Korpa je prazna");
        }

        double productsPrice = 0.0;
        for (CartItem item : cart.getItems()) {
            productsPrice += item.getPrice() * item.getQuantity();
        }
        double delivery = 500.0;
        double finalPrice = productsPrice + delivery;

        boolean discountApplied = false;
        if (user.getLoyaltyPoints() >= 50) {
            finalPrice = finalPrice * 0.9;
            user.setLoyaltyPoints(0);
            discountApplied = true;
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(shippingAddress);
        order.setOrderPrice(finalPrice);
        order.setStatus(DeliveryStatus.PENDING);

        Order saved = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem oi = new OrderItem();
            oi.setOrder(saved);
            oi.setProduct(cartItem.getProduct());
            oi.setQuantity(cartItem.getQuantity());
            oi.setUnitPrice(cartItem.getPrice());
            orderItems.add(oi);
        }
        orderItemRepository.saveAll(orderItems);

        user.setLoyaltyPoints(user.getLoyaltyPoints() + 10);
        appUserRepository.save(user);

        cartService.clearCart(userId);

        return new CreateOrderResult(discountApplied, finalPrice);
    }
}
