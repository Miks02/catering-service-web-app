package org.example.catering.cateringserviceapp.repository;

import org.example.catering.cateringserviceapp.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> FindByCartIdAndProductId(Long cartId, Long productId);
    List<CartItem> findByCartId(Long cartId);
}
