package org.example.catering.cateringserviceapp.repository;

import org.example.catering.cateringserviceapp.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(String userId);
}
